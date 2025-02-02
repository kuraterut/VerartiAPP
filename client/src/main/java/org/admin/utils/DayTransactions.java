package org.admin.utils;

import java.time.*;

public class DayTransactions extends Response{
	private LocalDate date;
	private Double cash;
	private Double card;
	private Double goods;

	public LocalDate getDate()	{return this.date;}
	public Double getCash()		{return this.cash;}
	public Double getCard()		{return this.card;}
	public Double getGoods()	{return this.goods;}

	public void setDate(LocalDate date)	{this.date=date;}
	public void setCash(Double cash)	{this.cash=cash;}
	public void setCard(Double card)	{this.card=card;}
	public void setGoods(Double goods)	{this.goods=goods;}

	public void addCash(Double cash)	{this.cash+=cash;}
	public void addCard(Double card)	{this.card+=card;}
	public void addGoods(Double goods)	{this.goods+=goods;}

	public void subCash(Double cash)	{this.cash-=cash;}
	public void subCard(Double card)	{this.card-=card;}
	public void subGoods(Double goods)	{this.goods-=goods;}
}