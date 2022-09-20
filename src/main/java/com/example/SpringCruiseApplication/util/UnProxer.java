package com.example.SpringCruiseApplication.util;

import com.example.SpringCruiseApplication.entity.Ship;
import com.example.SpringCruiseApplication.entity.Staff;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UnProxer {
    public static void setEnable(Object object,boolean status) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method setEnable = object.getClass().getMethod("setEnable", boolean.class);
        setEnable.invoke(object,status);
    }
    public static Ship getUnProxiedShip(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Ship ship = new Ship();
        Method getId = o.getClass().getMethod("getId");
        Method getPremiumTotalPlaces = o.getClass().getMethod("getPremiumTotalPlaces");
        Method getEconomTotalPlaces = o.getClass().getMethod("getEconomTotalPlaces");
        Method getMiddleTotalPlaces = o.getClass().getMethod("getMiddleTotalPlaces");
        Method getEconomCost = o.getClass().getMethod("getEconomCost");
        Method getMiddleCost = o.getClass().getMethod("getMiddleCost");
        Method getPremiumCost = o.getClass().getMethod("getPremiumCost");
        Method isEnable = o.getClass().getMethod("isEnable");
        Method getName = o.getClass().getMethod("getName");
        Method getTotalSeats= o.getClass().getMethod("getTotalSeats");


        ship.setTotalSeats((Integer) getTotalSeats.invoke(o));
        ship.setName((String)getName.invoke(o));
        ship.setId((Integer)getId.invoke(o));
        ship.setEnable((boolean)isEnable.invoke(o));
        ship.setPremiumCost((Integer) getPremiumCost.invoke(o));
        ship.setPremiumTotalPlaces((Integer) getPremiumTotalPlaces.invoke(o));
        ship.setEconomTotalPlaces((Integer) getEconomTotalPlaces.invoke(o));
        ship.setMiddleCost((Integer) getMiddleCost.invoke(o));
        ship.setMiddleTotalPlaces((Integer) getMiddleTotalPlaces.invoke(o));
        ship.setEconomCost((Integer) getEconomCost.invoke(o));
        return ship;

    }

    public static Staff getUnProxiedStaff(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Staff staff = new Staff();
        Method getId = o.getClass().getMethod("getId");
        Method getPosition = o.getClass().getMethod("getPosition");
        Method isEnable = o.getClass().getMethod("isEnable");
        Method getName = o.getClass().getMethod("getName");

        staff.setId((Long) getId.invoke(o));
        staff.setPosition((String) getPosition.invoke(o));
        staff.setEnable((boolean) isEnable.invoke(o));
        staff.setName((String) getName.invoke(o));
        return staff;
    }
}
