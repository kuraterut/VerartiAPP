package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
	"verarti/models"
	"verarti/pkg"
)

func checkRole(role string) (int, error) {
	if role == "master" {
		return 1, nil
	} else if role == "admin" {
		return 2, nil
	} else if role == "director" {
		return 3, nil
	}

	return 0, errors.New("invalid input.Role")
}

func (h *Handler) signUp(c *gin.Context) {
	var input models.Users

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	roleId, err := checkRole(input.Role)
	if err != nil {
		newErrorResponse(c, http.StatusForbidden, err.Error())
		return
	}

	id, err := h.services.Authorization.CreateUser(input, roleId)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"id": id,
	})
}

type signInInput struct {
	Phone    string `json:"phone" binding:"required"`
	Password string `json:"password" binding:"required"`
}

type getAuthorAndTokenResponse struct {
	Role  string `json:"role"`
	Token string `json:"token"`
}

func (h *Handler) signIn(c *gin.Context) {
	var input signInInput

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	token, role, err := h.services.Authorization.GenerateToken(input.Phone, input.Password)
	if err != nil {
		var errResp *pkg.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, getAuthorAndTokenResponse{
		Role:  role,
		Token: token,
	})
}
