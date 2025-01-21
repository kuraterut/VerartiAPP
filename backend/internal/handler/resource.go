package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
	"verarti/internal"
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

func (h *Handler) getResourceById(c *gin.Context) {
	resourceId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	resource, err := h.services.GetById(resourceId)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, resource)
}

func (h *Handler) getResourcesByMasterId(c *gin.Context) {
	masterId, err := getUserId(c)
	if err != nil {
		return
	}

	resources, err := h.services.Resource.GetByMasterId(masterId)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, getAllResourcesResponse{
		Data: resources,
	})
}

func (h *Handler) addResource(c *gin.Context) {
	masterId, err := getUserId(c)
	if err != nil {
		return
	}

	resourceId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	_, err = h.services.Resource.Add(masterId, resourceId)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, responseModel{
		Response: map[string]interface{}{
			"resourceId": resourceId,
		},
	})
}

func (h *Handler) createRequest(c *gin.Context) {

}

func (h *Handler) getRequests(c *gin.Context) {}

func (h *Handler) getResponseByRequestId(c *gin.Context) {}
