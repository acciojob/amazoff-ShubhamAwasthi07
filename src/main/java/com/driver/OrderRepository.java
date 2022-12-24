package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    private Map<String , Order> orderHashMap;
    private Map<String , Integer> deliveryPartnerHashMap;
    private Map<String , List<String>> partnerOrderMap;

    private Map<String , Integer> orderTimeMap;

    OrderRepository(){
        this.orderHashMap = new HashMap<>();
        this.deliveryPartnerHashMap = new HashMap<>();
        this.partnerOrderMap = new HashMap<>();
        this.orderTimeMap = new HashMap<>();
    }

    public void addOrder(Order order){
        orderHashMap.put(order.getId() , order);
    }

    public void addDeliveryPartner(String partnerId){
        deliveryPartnerHashMap.put(partnerId , deliveryPartnerHashMap.getOrDefault(partnerId , 0)+1);
    }

    public void addOrderPartnerPair(String partnerId , String orderId){
        List<String> temp = partnerOrderMap.get(partnerId);
        if(temp==null){
            temp = new ArrayList<>();
            temp.add(orderId);
            partnerOrderMap.put(partnerId , temp);
        }
        else {
            temp.add(orderId);
        }
    }

    public Order getOrderByOrderId(String orderId){
        if(orderHashMap.containsKey(orderId)) return orderHashMap.get(orderId);
        return new Order("Not Exist" , "Not Exist");
    }

    public DeliveryPartner getPartnerByPartnerId(String partnerId){
        if(partnerOrderMap.containsKey(partnerId)){
            DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartnerHashMap.get(partnerId));
            return deliveryPartner;
        }
        return new DeliveryPartner("Not Exist");
    }

    public int getNumberOfOrdersAssigned(String partnerId){
        if(partnerOrderMap.containsKey(partnerId)) return partnerOrderMap.get(partnerId).size();
        return 0;
    }

    public List<String> getAllTheOrdersOfAPartner(String partnerId){
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrdersFromSystem(){
        List<String> ans = new ArrayList<>();
        for(String id : orderHashMap.keySet()){
            ans.add(id);
        }
        return ans;
    }

    public int getNotAssignedOrders(){
        int sum = 0;
        for(String id : partnerOrderMap.keySet()){
            sum += partnerOrderMap.get(id).size();
        }
        return orderHashMap.size()-sum;
    }

    public int getCountOfUndeliveredOrders(String partnerId , String time){
        int time_int = Integer.valueOf(time.substring(0 , 2))*60 + Integer.valueOf(time.substring(3 , 5));
        int count = 0;
        List<String> orderIds = partnerOrderMap.get(partnerId);
        for(String id : orderIds){
            int temp = orderHashMap.get(id).getDeliveryTime();
            if(temp>time_int)count++;
        }
        return count;
    }

    public String getTheLastTime(String partnerId){
        List<String> orderIds = partnerOrderMap.get(partnerId);
        int maxi = 0;
        for(String id : orderIds){
            maxi = Math.max(maxi , orderHashMap.get(id).getDeliveryTime());
        }
        int minutes = maxi%60;
        int hours = (maxi - minutes)/60;
        String ans = ""+hours+":"+minutes+"";
        return ans;
    }

    public void deletePartnerById(String partnerId){
        deliveryPartnerHashMap.remove(partnerId);
        if(partnerOrderMap.containsKey(partnerId)){
            partnerOrderMap.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId){
        orderHashMap.remove(orderId);
        for(String id : partnerOrderMap.keySet()){
            List<String> list = partnerOrderMap.get(id);
            if(list.contains(orderId)){
                deliveryPartnerHashMap.put(id , deliveryPartnerHashMap.get(id)-1);
                list.remove(String.valueOf(orderId));
                break;
            }
        }
    }

}