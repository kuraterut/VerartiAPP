package src.admin.utils;

public class AdminInfo extends Response{
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String bio;
	
	public Boolean equals(AdminInfo other){
		return this.id == other.getId();
	}

	public Long getId()						{return this.id;}
	public String getName()					{return this.name;}
	public String getSurname()				{return this.surname;}
	public String getPatronymic()			{return this.patronymic;}
	public String getBio()					{return this.bio;}
	public String getFio()					{return surname+" "+name+" "+patronymic;}

	public void setId(Long id)							{this.id = id;}
	public void setName(String name)					{this.name = name;}
	public void setSurname(String surname)				{this.surname = surname;}
	public void setPatronymic(String patronymic)		{this.patronymic = patronymic;}
	public void setBio(String bio)						{this.bio = bio;}
	
}