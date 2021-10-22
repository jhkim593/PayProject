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

    @Column(nullable = false,length = 500)
    private String data;

    @Column(nullable = false, unique = true)
    private String payId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="parentPay")
    private Pay parentPay;

    @OneToMany(mappedBy = "parentPay")
    private List<Pay>cancelPayList=new ArrayList<>();

    private LocalDateTime regDate;

//    @Version
//    private Long version;


    public static Pay createPay(String data,Pay parentPay,String payId){
        Pay pay=new Pay();
        pay.data=data;
        if(parentPay!=null){
            pay.addParentPay(parentPay);
        }
        pay.payId=payId;
        pay.regDate=LocalDateTime.now().withNano(0);
        return pay;
    }
    public void addParentPay(Pay parentPay){
        this.parentPay=parentPay;
        parentPay.getCancelPayList().add(this);
    }
}
