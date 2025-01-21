package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"verarti/models"
)

func (h *Handler) createClient(c *gin.Context) {
	var input models.Client

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	id, err := h.services.Client.CreateClient(input)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"id": id,
	})
}

func (h *Handler) getAllClients(c *gin.Context) {}

func (h *Handler) getClientById(c *gin.Context) {}

func (h *Handler) updateClient(c *gin.Context) {}
