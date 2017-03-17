package com.egs.wogal.camera_app.For_Sale_Object;

import com.egs.wogal.camera_app.Car;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by wogal on 3/3/2017.
 */

public class Serialize_DeSerilize_Object {
    public static void serialize(List<Car> pList, String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(pList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Car> desrialize(String fileName) {
        List<Car> pList = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            pList = (List<Car>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (pList);
    }
}

//   public List<Car> deserialize_Ob

