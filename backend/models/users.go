package models

type Users struct {
	Id         int    `json:"-" db:"id"`
	Name       string `json:"name" binding:"required" db:"name"`
	Surname    string `json:"surname" binding:"required" db:"surname"`
	Patronymic string `json:"patronymic" binding:"required" db:"patronymic"`
	Password   string `json:"password" binding:"required" db:"password_hash"`
	Email      string `json:"email" binding:"required" db:"email"`
	Phone      string `json:"phone" binding:"required" db:"phone"`
	PfpUrl     string `json:"pfp_url" db:"pfp_url"`
	Bio        string `json:"bio" db:"bio"`
	Role       string `json:"role" binding:"required" db:"role"`
	Salary     string `json:"salary" db:"salary"`
}
