package com.driver;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private HashMap<String , Order> orderMap;
    private HashMap<String ,Integer> deliveryPartnerMap;
    private HashMap<String , List<String>>orderPartnerMap;

    private Map<String , Integer> orderTimeMap;


    public OrderRepository() {
        this.orderMap = new HashMap<String , Order>();
        this.deliveryPartnerMap = new HashMap<String , Integer>();
        this.orderPartnerMap = new HashMap<String , List<String>>();
        this.orderTimeMap = new HashMap<String , Integer>();
    }

    public void addOrderInDB(Order order){
        orderMap.put(order.getId() , order);
    }

    public void addDeliveryPartnerInDB(String partnerId){
        deliveryPartnerMap.put(partnerId , deliveryPartnerMap.getOrDefault(partnerId , 0) + 1);
    }

    public void addOrderPartnerPairInDB(String orderId , String partnerId){

        List<String> temp = orderPartnerMap.get(partnerId);
        if(temp==null){
            temp = new ArrayList<>();
            temp.add(orderId);
            orderPartnerMap.put(partnerId , temp);
        }
        else {
            temp.add(orderId);
        }

    }

    public Order getOrderById(String orderId){
        if(orderMap.containsKey(orderId))
            return orderMap.get(orderId);
        return new Order("Not Exist" , "Not Exist");
    }

    public DeliveryPartner getPartnerById(String partnerId){
        if(orderPartnerMap.containsKey(partnerId)){
            DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartnerMap.get(partnerId));
            return deliveryPartner;
        }
        return new DeliveryPartner("Not Exist");
    }

    public int getOrderCountByPartnerId(String partnerId){
        int cnt = 0;
        if(orderPartnerMap.containsKey(partnerId)){
            cnt = orderPartnerMap.get(partnerId).size();
        }
        return cnt;
    }

    public List<String> getOrdersByPartnerId(String partnerId){

        List<String> Orders = new ArrayList<>();

        if(deliveryPartnerMap.containsKey(partnerId)){
            Orders = orderPartnerMap.get(partnerId);
        }

        return Orders;

    }

    public List<String> getAllOrder(){

        Set<String> getOrders = new HashSet<>();
        getOrders = orderMap.keySet();

        List<String> getOrderList = new ArrayList<>();

        for(String ord : getOrders){
            getOrderList.add(ord);
        }

        return getOrderList;
    }

    public int getCountOfUnassignedOrders(){
        int count = 0;

        for(String partner : orderPartnerMap.keySet()){
            count = count + orderPartnerMap.get(partner).size();
        }
        return (orderMap.size() - count);
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time , String partnerId){
        int count = 0;
        int time1 = (Integer.parseInt(time.substring(0,2))*60) + (Integer.parseInt(time.substring(time.length()-2 , time.length())));

        for(String order : orderPartnerMap.get(partnerId)){
            if(orderMap.get(order).getDeliveryTime() > time1){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){

        int time = 0;
        for(String order : orderPartnerMap.get(partnerId)){
            if(time < orderMap.get(order).getDeliveryTime()){
                time = orderMap.get(order).getDeliveryTime();
            }
        }
        int t = time/60;
        String hr = String.format("%2d",t);
        String min = String.format("%2d" ,time%60);

        return hr+":"+min;
    }

    public void deletePartnerById(String partnerId){

        if(orderPartnerMap.containsKey(partnerId)){
            for(String order : orderPartnerMap.get(partnerId)){
                if(orderMap.containsKey(order)){
                    orderMap.remove(order);
                }
            }
        }
        orderPartnerMap.remove(partnerId);

        if(deliveryPartnerMap.containsKey(partnerId)){
            deliveryPartnerMap.remove(partnerId);
        }
    }
    public void deleteOrderById(String orderId){

        List<String> Orderlist = new ArrayList<>();

        for(String partner : orderPartnerMap.keySet()){
            for(String order : orderPartnerMap.get(partner)){
                if(order.equals(orderId)){
                    Orderlist = orderPartnerMap.get(partner);
                    Orderlist.remove(order);

                    orderPartnerMap.put(partner , Orderlist);

                    break;
                }
            }
        }
        if(orderMap.containsKey(orderId)){
            orderMap.remove(orderId);
        }
    }
}