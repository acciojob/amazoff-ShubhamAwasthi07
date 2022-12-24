package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String , Order> orderMap;
    private HashMap<String ,DeliveryPartner> deliveryPartnerMap;
    private HashMap<String , String> orderAndDeliveryPartnerMap;
    private HashMap<String , String> partnerLastorderMap;

    public OrderRepository() {
        this.orderMap = new HashMap<String , Order>();
        this.deliveryPartnerMap = new HashMap<String , DeliveryPartner>();
        this.orderAndDeliveryPartnerMap = new HashMap<String , String>();
        this.partnerLastorderMap = new HashMap<String , String>();
    }

    public void addOrderInDB(Order order){
        orderMap.put(order.getId() , order);
    }

    public void addDeliveryPartnerInDB(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId , deliveryPartner);
    }

    public void addOrderPartnerPairInDB(String orderId , String partnerId){

        orderAndDeliveryPartnerMap.put(orderId , partnerId);

        partnerLastorderMap.put(partnerId , orderId);

        if(deliveryPartnerMap.containsKey(partnerId)){
            int count = deliveryPartnerMap.get(partnerId).getNumberOfOrders();
            count++;
            deliveryPartnerMap.get(partnerId).setNumberOfOrders(count);
        }

    }

    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        Integer cnt = null;
        if(deliveryPartnerMap.containsKey(partnerId)){
            cnt = deliveryPartnerMap.get(partnerId).getNumberOfOrders();
        }
        return cnt;
    }

    public List<String> getOrdersByPartnerId(String partnerId){

        List<String> arr = new ArrayList<>();

        for(String orderId : orderAndDeliveryPartnerMap.keySet()){
            if(orderAndDeliveryPartnerMap.get(orderId).equalsIgnoreCase(partnerId)){

                arr.add(orderId);
            }
        }
        return arr;
    }

    public List<String> getAllOrder(){

        List<String> ans = new ArrayList<>(orderMap.keySet());
        return ans;
    }

    public Integer getCountOfUnassignedOrders(){
        Integer count = 0;
        for(String orderId : orderMap.keySet()){
            if(!orderAndDeliveryPartnerMap.containsKey(orderId))
                count++;
        }
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time , String partnerId){
        Integer count = 0;
        for(String orderId : orderAndDeliveryPartnerMap.keySet()){
            if(orderAndDeliveryPartnerMap.get(orderId).equalsIgnoreCase(partnerId)){

                Order order = orderMap.get(orderId);
                if(order.getDeliveryTime() > order.convert(time)){
                count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        if(!deliveryPartnerMap.containsKey(partnerId) || orderMap.get(partnerLastorderMap.get(partnerId)) == null)
            return "";

        int time = orderMap.get(partnerLastorderMap.get(partnerId)).getDeliveryTime();

        int hr = time/60;
        int min = time%60;
        String hour = String.valueOf(hr);
        String minute = String.valueOf(min);

        if(minute.length() == 1)
            minute = "0" + minute;
        if(hour.length() == 1)
            hour = "0" + hour;



        return hour + ":" + minute;
    }

    public void deletePartnerById(String partnerId){
        if(deliveryPartnerMap.containsKey(partnerId)){
            deliveryPartnerMap.remove(partnerId);
        }
        List<String> temp = new ArrayList<>(orderAndDeliveryPartnerMap.keySet());

        for(String orderId : temp){
            if(orderAndDeliveryPartnerMap.get(orderId).equalsIgnoreCase(partnerId)){
                orderAndDeliveryPartnerMap.remove(orderId);
            }
        }
    }
    public void deleteOrderById(String orderId){
        if(orderMap.containsKey(orderId))
            orderMap.remove(orderId);

        if(orderAndDeliveryPartnerMap.containsKey(orderId))
            orderAndDeliveryPartnerMap.remove(orderId);
    }
}