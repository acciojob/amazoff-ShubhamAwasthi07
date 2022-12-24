package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private HashMap<String , Order> orderMap;
    private HashMap<String ,DeliveryPartner> deliveryPartnerMap;

    private HashMap<String , List<String>>orderPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<String , Order>();
        this.deliveryPartnerMap = new HashMap<String , DeliveryPartner>();
        this.orderPartnerMap = new HashMap<String , List<String>>();
    }

    public void addOrderInDB(Order order){
        orderMap.put(order.getId() , order);
    }

    public void addDeliveryPartnerInDB(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId , deliveryPartner);
    }

    public void addOrderPartnerPairInDB(String orderId , String partnerId){

       List<String> orders = new ArrayList<>();
       if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(partnerId)){
           orderMap.put(orderId , orderMap.get(orderId));
           deliveryPartnerMap.put(partnerId , deliveryPartnerMap.get(partnerId));

           if(orderPartnerMap.containsKey(partnerId)){
               orders = orderPartnerMap.get(partnerId);
           }
           orders.add(orderId);

           deliveryPartnerMap.get(partnerId).setNumberOfOrders(orders.size());
       }
       orderPartnerMap.put(partnerId , orders);

    }

    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        int cnt = 0;
        if(deliveryPartnerMap.containsKey(partnerId)){
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

    public Integer getCountOfUnassignedOrders(){
        int count = 0;

        for(String partner : orderPartnerMap.keySet()){
            count = count + orderPartnerMap.get(partner).size();
        }
        return (orderMap.size() - count);
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time , String partnerId){
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