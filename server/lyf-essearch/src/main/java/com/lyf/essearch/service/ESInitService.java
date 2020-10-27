package com.lyf.essearch.service;

import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.model.FoodSpecialModel;
import com.lyf.common.model.TopicInfosModel;

import java.io.IOException;
import java.util.List;

/**
 * @author lyf
 * @date 2020/10/19-17:45
 */
public interface ESInitService {
    int initEs(List<FoodInfosModel> models) throws IOException;

    int initTopicsEs(List<TopicInfosModel> models) throws IOException;

    int initSpecialsEs(List<FoodSpecialModel> models) throws IOException;


}
