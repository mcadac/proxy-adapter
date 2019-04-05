package com.payu.client;

import java.util.*;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import javafx.util.*;
import retrofit2.*;
import retrofit2.Response;
import retrofit2.converter.gson.*;

import com.payu.model.*;

@Service
public class ProxyService {

    private List<ProxyInterface> proxyClients = new ArrayList<>();


    private String myHost;


    @Autowired
    public ProxyService(@Value("${proxy.urls}") String[] proxyUrls,
                        @Value("${proxy.host}") String myHost){

        this.myHost = myHost;

        Arrays.stream(proxyUrls)
                .forEach(url ->
                    proxyClients.add(new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ProxyInterface.class))
                );
    }

    public String getLeader() throws Exception{

        System.out.println("Assigning leader process...");
        if(proxyClients.isEmpty()){
            return GeneralState.getPriority().toString();
        } else{
            return assignNewLeader();
        }
    }


    /**
     *
     * @return
     */
    public void getPriority(){

        if(GeneralState.getCurrentLeader() != null) {
            System.out.println("Ping leader...");
            Retrofit retrofitCall = new Retrofit.Builder()
                    .baseUrl(GeneralState.getCurrentLeader().getKey())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProxyInterface proxyLeader = retrofitCall.create(ProxyInterface.class);
            String response = sendMessage(proxyLeader.getPriority());

            if (response.equals("-1")) {

                System.out.println("Leader is down... setting new leader : " + assignNewLeader());
            } else {
                System.out.println("Current leader alive : " + response);
            }
        }else{
            System.out.println("Leader is not assign");
            assignNewLeader();
        }

    }

    private String assignNewLeader() {
        final String currentLedaer = proxyClients.parallelStream()
                .map(client ->{ return Integer.valueOf(sendMessage(client.getPriority())); })
                .filter(priority -> priority != -1 && priority.compareTo(GeneralState.getPriority()) > 0)
                .max((result1, result2) -> result1.compareTo(result2) > 0 ? result1 : result2)
                .orElse(GeneralState.getPriority())
                .toString();

        if(currentLedaer.equalsIgnoreCase(GeneralState.getPriority().toString())){
            postLeader();
        }

        return currentLedaer;
    }


    private void postLeader(){

        GeneralState.setCurrentLeader(new Pair<>(myHost,myHost.split("//")[1]));
        final Leader leader = new Leader(myHost, GeneralState.getPriority().toString());

        proxyClients.stream()
                .map(client ->{
                    return CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName());
                        return sendMessage(client.postLeader(leader));
                    });
                })
                .forEach(future -> System.out.println(Thread.currentThread().getName()));

    }

    private String sendMessage(Call<String> retrofitCall ){

        try{
            Response<String> response = retrofitCall.execute();

            if (!response.isSuccessful()) {
                System.out.println("unsuccessful");
                return "-1";
            }

            System.out.println("Response priority service : " + response.body());
            return response.body();

        }catch (Exception ex){
            System.out.println("Execption sending message: " + ex.getMessage());
            return "-1";
        }

    }

    public String getMyHost() {
        return myHost;
    }
}
