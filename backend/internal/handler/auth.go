package handler

import (
	"errors"
	"fmt"
	"github.com/gin-gonic/gin"
	"net/http"
	"verarti/internal"
	"verarti/models"
)

func checkRoles(roles []string) ([]int, error) {
	roleIds := make([]int, len(roles))

	for i, role := range roles {
		if role == "master" {
			roleIds[i] = 1
		} else if role == "admin" {
			roleIds[i] = 2
		} else {
			return nil, errors.New(fmt.Sprintf("invalid role: %s", role))
		}
	}

	return roleIds, nil
}

func (h *Handler) signUp(c *gin.Context) {
	var input models.Users

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	roleIds, err := checkRoles(input.Roles)
	if err != nil {
		newErrorResponse(c, http.StatusForbidden, err.Error())
		return
	}

	id, err := h.services.Authorization.CreateUser(input, roleIds)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

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
	Role     string `json:"role" binding:"required"`
}

func (h *Handler) signIn(c *gin.Context) {
	var input signInInput

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	token, err := h.services.Authorization.GenerateToken(input.Phone, input.Password, input.Role)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"token": token,
	})
}
