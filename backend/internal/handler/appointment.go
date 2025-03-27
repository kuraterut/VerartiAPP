package handler

import (
	"github.com/gin-gonic/gin"
	"github.com/pkg/errors"
	"net/http"
	"strconv"
	"verarti/internal/domain"
	"verarti/models"
)

func (h *Handler) putAdminToDate(c *gin.Context) {
	var input models.AdminShift
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateAndTimeFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err = h.services.Appointment.PutAdminToDate(input)
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

func (h *Handler) putMasterToDate(c *gin.Context) {
	var input models.MasterShift
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateAndTimeFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err = h.services.Appointment.PutMasterToDate(input)
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

func (h *Handler) getAdminByDate(c *gin.Context) {
	date := c.Query("date")
	if date == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	err := domain.ValidatorDateAndTimeFormat("2006-01-02", date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	admin, err := h.services.Appointment.GetAdminByDate(date)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, admin)
}

func (h *Handler) getAllMastersByDate(c *gin.Context) {
	date := c.Query("date")
	if date == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	err := domain.ValidatorDateAndTimeFormat("2006-01-02", date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	appointed := c.Query("appointed")
	if appointed == "" {
		newErrorResponse(c, http.StatusBadRequest, "appointed is required")
		return
	}

	isAppointed := true
	if appointed == "false" {
		isAppointed = false
	} else if appointed != "true" {
		newErrorResponse(c, http.StatusBadRequest, "invalid 'appointed' parameter")
	}

	masters, err := h.services.Appointment.GetAllMastersByDate(date, isAppointed)
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
		"masters": masters,
	})
}

func (h *Handler) createAppointment(c *gin.Context) {
	var input models.MasterAppointmentInput
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateAndTimeFormat("2006-01-02", input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err = domain.ValidatorDateAndTimeFormat("15:04", input.StartTime)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	appointmentId, err := h.services.Appointment.CreateAppointment(input)
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
		"appointment_id": appointmentId,
	})
}

func (h *Handler) getAppointmentByClientId(c *gin.Context) {
	clientId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	appointments, err := h.services.Appointment.GetAppointmentByClientId(clientId)
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
		"appointments": appointments,
	})
}

func (h *Handler) getDailyAppointment(c *gin.Context) {}

func (h *Handler) getMonthlyAppointment(c *gin.Context) {}

func (h *Handler) cancellationRequest(c *gin.Context) {}
