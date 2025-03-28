package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
	"verarti/internal/domain"
	"verarti/models"
)

func (h *Handler) createClient(c *gin.Context) {
	var input models.Client

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	if input.Birthday != "" {
		err := domain.ValidatorDateAndTimeFormat("2006-01-02", input.Birthday)
		if err != nil {
			newErrorResponse(c, http.StatusBadRequest, err.Error())
			return
		}
	}

	id, err := h.services.Client.CreateClient(input)
	if err != nil {
		var errResp *domain.ErrorResponse
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

func (h *Handler) getAllClients(c *gin.Context) {
	clients, err := h.services.Client.GetAllClients()
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"clients": clients,
	})
}

func (h *Handler) getClientById(c *gin.Context) {
	clientId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	client, err := h.services.Client.GetClientById(clientId)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, client)
}

func (h *Handler) getClientByPhone(c *gin.Context) {
	phone := c.Query("phone")
	if phone == "" {
		newErrorResponse(c, http.StatusBadRequest, "phone number is required")
		return
	}

	client, err := h.services.Client.GetClientByPhone(phone)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, client)
}

func (h *Handler) updateClient(c *gin.Context) {
	clientId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	var input models.ClientUpdate
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	if input.Birthday != "" {
		err := domain.ValidatorDateAndTimeFormat("2006-01-02", input.Birthday)
		if err != nil {
			newErrorResponse(c, http.StatusBadRequest, err.Error())
			return
		}
	}

	client := models.Client{
		Name:       input.Name,
		Surname:    input.Surname,
		Patronymic: input.Patronymic,
		Phone:      input.Phone,
		Email:      input.Email,
		Comment:    input.Comment,
		Birthday:   input.Birthday,
	}

	err = h.services.Client.UpdateClient(clientId, client)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, "OK")
}
