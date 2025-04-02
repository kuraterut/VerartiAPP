package org.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Product extends Response {
	private Long id;
	private String name;
	private Long price;
	private Integer count;

	public Product(int code, String message) {
		this.setCode(code);
		this.setMsg(message);
	}

	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof Product product)) return false;
        return Objects.equals(this.id, product.getId());
	}

	public JSONObject toJson(){
		JSONObject obj = new JSONObject();

		obj.put("name", this.name);
		obj.put("price", this.price);
		obj.put("count", this.count);

		return obj;
	}

	public static Product fromJson(JSONObject obj){
		Product product = new Product();

		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		Long price = (Long) obj.get("price");
		Integer count = ((Long) obj.get("count")).intValue();


		product.setId(id);
		product.setName(name);
		product.setPrice(price);
		product.setCount(count);

		product.setCode(200);
		return product;
	}

	@Override
	public String toString() {
		return getId() + " " + getName();
	}
}