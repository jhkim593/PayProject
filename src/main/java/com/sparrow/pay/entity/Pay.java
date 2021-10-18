package com.sparrow.pay.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="parentPay")
    private Pay parentPay;

    @OneToMany(mappedBy = "parentPay")
    private List<Pay>cancelPayList=new ArrayList<>();

    private LocalDateTime regDate;


    public static Pay createPay(String data,Pay parentPay){
        Pay pay=new Pay();
        pay.data=data;
        if(parentPay!=null){
            pay.addParentPay(parentPay);
        }
        pay.regDate=LocalDateTime.now().withNano(0);
        return pay;
    }
    public void addParentPay(Pay parentPay){
        this.parentPay=parentPay;
        parentPay.getCancelPayList().add(this);
    }
}
