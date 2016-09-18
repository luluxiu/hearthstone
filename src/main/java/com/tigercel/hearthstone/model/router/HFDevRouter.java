package com.tigercel.hearthstone.model.router;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tigercel.hearthstone.model.HFDevRouterGroup;
import com.tigercel.hearthstone.model.HFUser;
import com.tigercel.hearthstone.model.base.HFDevice;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by somebody on 2016/8/1.
 */
@Entity
@Data
@Table(name = "hf_router")
@JsonIgnoreProperties(value = {"status", "users", "group"})
public class HFDevRouter extends HFDevice {

    @Column(length = 18)
    private String ip;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HFDevRouterStatus status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "hf_user_router",
            joinColumns = {@JoinColumn(name = "router_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName ="id")})
    private Collection<HFUser> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private HFDevRouterGroup group;

}