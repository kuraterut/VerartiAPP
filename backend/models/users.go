package models

type Users struct {
	Id         int    `json:"-" db:"id"`
	Name       string `json:"name" binding:"required"`
	Surname    string `json:"surname" binding:"required"`
	Patronymic string `json:"patronymic" binding:"required"`
	Password   string `json:"password" binding:"required"`
	Email      string `json:"email" binding:"required"`
	Phone      string `json:"phone" binding:"required"`
	PfpUrl     string `json:"pfp_url"`
	Bio        string `json:"bio"`
	Role       string `json:"role" binding:"required"`
	Salary     string `json:"salary"`
}
