package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.StrainDataModel;

import java.util.ArrayList;

public class StrainTestData {

    public static ArrayList<StrainDataModel> get_strain_data(){
        ArrayList<StrainDataModel> dataModels = new ArrayList<>();
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "H" , "200 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 4.0 , "S" , "240 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "S" , "100 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "I" , "20 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "H" , "230 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "I" , "142 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "S" , "40 Reviews"));
        dataModels.add(new StrainDataModel("Cactus Sunset" , 3.5 , "H" , "210 Reviews"));
        return  dataModels;
    }
}
