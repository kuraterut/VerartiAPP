package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"verarti/internal/domain"
	"verarti/models"
)

func (h *Handler) putAdminToDate(c *gin.Context) {
	var input models.AdminShift
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err = h.services.Schedule.PutAdminToDate(input)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, "OK")
}

func (h *Handler) putMasterToDate(c *gin.Context) {
	var input models.MasterShift
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err = h.services.Schedule.PutMasterToDate(input)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, "OK")
}

func (h *Handler) getAdminByDate(c *gin.Context) {
	var input models.DateInput
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	admin, err := h.services.Schedule.GetAdminByDate(input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, admin)
}

func (h *Handler) getAllMastersByDate(c *gin.Context) {
	var input models.DateInput
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	masters, err := h.services.Schedule.GetAllMastersByDate(input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"masters": masters,
	})
}

func (h *Handler) getDailySchedule(c *gin.Context) {}

func (h *Handler) getMonthlySchedule(c *gin.Context) {}

func (h *Handler) cancellationRequest(c *gin.Context) {}
