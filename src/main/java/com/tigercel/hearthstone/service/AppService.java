package com.tigercel.hearthstone.service;

import com.tigercel.hearthstone.model.app.HFApp;
import com.tigercel.hearthstone.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by somebody on 2016/8/12.
 */
@Service
public class AppService {


    @Autowired
    private AppRepository appRepository;


    public List<HFApp> findAll() {
        return appRepository.findAll();
    }

    public HFApp findOne() {
        List<HFApp> apps = findAll();

        if(apps.isEmpty()) {
            return null;
        }
        else {
            return apps.get(0);
        }
    }

    public HFApp findFirst() {
        Page<HFApp> apps = appRepository.findAll(new PageRequest(0, 1));

        if(apps == null || apps.hasContent() == false) {
            return null;
        }
        else {
            return apps.getContent().get(0);
        }
    }

    public HFApp findOne(Long id) {
        return appRepository.findOne(id);
    }


}
