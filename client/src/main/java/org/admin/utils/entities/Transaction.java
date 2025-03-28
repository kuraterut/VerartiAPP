package org.admin.utils.entities;

import org.admin.utils.Response;

import java.time.LocalDateTime;

public class Transaction extends Response {
    private Long id;
    private TransactionType type;
    private Long amount;
    private Integer count;
    private LocalDateTime timestamp;
    private Long unitId;

    public Long getId()                 {return this.id;}
    public TransactionType getType()    {return this.type;}
    public Long getAmount()             {return this.amount;}
    public Integer getCount()           {return this.count;}
    public LocalDateTime getTimestamp() {return this.timestamp;}
    public Long getUnitId()             {return this.unitId;}


    public void setId(Long id)                          { this.id = id;}
    public void setType(TransactionType type)           { this.type = type;}
    public void setAmount(Long amount)                  { this.amount = amount;}
    public void setCount(Integer count)                 { this.count = count;}
    public void setTimestamp(LocalDateTime timestamp)   { this.timestamp = timestamp;}
    public void setUnitId(Long unitId)                  { this.unitId = unitId;}
}
