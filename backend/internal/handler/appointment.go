package handler

import (
	"fmt"
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

	err := domain.ValidateDateOnly(input.Date)
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

	c.JSON(http.StatusOK, domain.StatusOK)
}

func (h *Handler) putMasterToDate(c *gin.Context) {
	var input models.MasterShift
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidateDateOnly(input.Date)
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

	c.JSON(http.StatusOK, domain.StatusOK)
}

func (h *Handler) cancelMasterEntryForDate(c *gin.Context) {
	date := c.Query("date")
	if date == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	err := domain.ValidateDateOnly(date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	masterIdStr := c.Query("master_id")
	if masterIdStr == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	masterId, err := strconv.Atoi(masterIdStr)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, fmt.Sprintf("invalid master id: %s", masterIdStr))
	}

	err = h.services.Appointment.CancelMasterEntryForDate(masterId, date)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, domain.StatusOK)
}

func (h *Handler) getAdminByDate(c *gin.Context) {
	date := c.Query("date")
	if date == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	err := domain.ValidateDateOnly(date)
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

	err := domain.ValidateDateOnly(date)
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

	err := domain.ValidateDateOnly(input.Date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err = domain.ValidateTimeOnly(input.StartTime)
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

func (h *Handler) getAllAppointmentsByDate(c *gin.Context) {
	date := c.Query("date")
	if date == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	err := domain.ValidateDateOnly(date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	appointments, err := h.services.Appointment.GetAllAppointmentsByDate(date)
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

func (h *Handler) getAppointmentById(c *gin.Context) {
	appointmentId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	appointment, err := h.services.Appointment.GetAppointmentById(appointmentId)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, appointment)
}

func (h *Handler) deleteAppointmentById(c *gin.Context) {
	appointmentId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	err = h.services.Appointment.DeleteAppointmentById(appointmentId)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, domain.StatusOK)
}

func (h *Handler) updateAppointmentById(c *gin.Context) {
	appointmentId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	var input models.MasterAppointmentUpdate
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err = h.services.Appointment.UpdateAppointmentById(appointmentId, input)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, domain.StatusOK)
}

func (h *Handler) getMonthlySchedule(c *gin.Context) {
	monthStr := c.Query("month")
	if monthStr == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	month, err := strconv.Atoi(monthStr)
	if err != nil || (month < 1 || month > 12) {
		newErrorResponse(c, http.StatusBadRequest, fmt.Sprintf("invalid month param = %s", monthStr))
	}

	yearStr := c.Query("year")
	if yearStr == "" {
		newErrorResponse(c, http.StatusBadRequest, "year is required")
		return
	}

	year, err := strconv.Atoi(yearStr)
	if err != nil || year <= 0 {
		newErrorResponse(c, http.StatusBadRequest, fmt.Sprintf("invalid year param = %s", yearStr))
	}

	schedules, err := h.services.Appointment.GetMonthlySchedule(year, month)
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
		"schedules": schedules,
	})
}

func (h *Handler) GetAppointmentsForMasterByDate(c *gin.Context) {
	masterId, err := getUserId(c)
	if err != nil {
		return
	}

	date := c.Query("date")
	if date == "" {
		newErrorResponse(c, http.StatusBadRequest, "date is required")
		return
	}

	err = domain.ValidateDateOnly(date)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	appointments, err := h.services.Appointment.GetAllAppointmentsByDateAndMasterId(masterId, date)
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
