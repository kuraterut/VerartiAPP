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

	err := domain.ValidatorDateFormat("2006-01-02", input.Day)
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

func (h *Handler) putMasterToDate(c *gin.Context) {}

func (h *Handler) getAdminByDate(c *gin.Context) {}

func (h *Handler) getAllMastersByDate(c *gin.Context) {}

func (h *Handler) getDailySchedule(c *gin.Context) {}

func (h *Handler) getMonthlySchedule(c *gin.Context) {}

func (h *Handler) cancellationRequest(c *gin.Context) {}
