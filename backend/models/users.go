package models

type Users struct {
	Id         int      `json:"id" db:"id"`
	Name       string   `json:"name" binding:"required" db:"name"`
	Surname    string   `json:"surname" binding:"required" db:"surname"`
	Patronymic string   `json:"patronymic" binding:"required" db:"patronymic"`
	Password   string   `json:"password" binding:"required" db:"password_hash"`
	Email      string   `json:"email" binding:"required" db:"email"`
	Phone      string   `json:"phone" binding:"required" db:"phone"`
	Photo      string   `json:"photo" db:"photo"`
	Bio        string   `json:"bio" db:"bio"`
	Roles      []string `json:"roles" binding:"required" db:"roles"`
	CurSalary  string   `json:"current_salary" db:"current_salary"`
}

type Info struct {
	Name       string `json:"name" binding:"required" db:"name"`
	Surname    string `json:"surname" binding:"required" db:"surname"`
	Patronymic string `json:"patronymic" binding:"required" db:"patronymic"`
	Email      string `json:"email" binding:"required" db:"email"`
	Phone      string `json:"phone" binding:"required" db:"phone"`
	Bio        string `json:"bio" binding:"required" db:"bio"`
}

type UpdatePasswordInput struct {
	OldPassword string `json:"old_password" binding:"required"`
	NewPassword string `json:"new_password" binding:"required"`
}

type MasterIdInput struct {
	MasterId int `json:"master_id" binding:"required"`
}
