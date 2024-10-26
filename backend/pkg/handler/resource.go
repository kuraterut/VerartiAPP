package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"verarti/models"
)

func (h *Handler) createResource(c *gin.Context) {
	var input models.Resource
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	resourceId, err := h.services.Resource.Create(input)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"resourceId": resourceId,
	})
}

type getAllResourcesResponse struct {
	Data []models.Resource `json:"data"`
}

func (h *Handler) getAllResources(c *gin.Context) {
	resources, err := h.services.Resource.GetAll()
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, getAllResourcesResponse{
		Data: resources,
	})
}

func (h *Handler) getResourceById(c *gin.Context) {}

func (h *Handler) createRequest(c *gin.Context) {}

func (h *Handler) getRequests(c *gin.Context) {}

func (h *Handler) getResponseByRequestId(c *gin.Context) {}
