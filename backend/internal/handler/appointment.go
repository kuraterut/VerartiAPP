package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"verarti/internal/domain"
	"verarti/models"
)

func (h *Handler) createAppointment(c *gin.Context) {
	var input models.Appointment
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := domain.ValidatorDateFormat("15:04", input.Duration)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	appointmentId, err := h.services.Appointment.CreateAppointment(input)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"appointmentId": appointmentId,
	})
}

func (h *Handler) getAllAppointments(c *gin.Context) {}

func (h *Handler) getAppointmentById(c *gin.Context) {}

func (h *Handler) addAppointmentForMaster(c *gin.Context) {}

func (h *Handler) updateAppointment(c *gin.Context) {}

func (h *Handler) deleteAppointment(c *gin.Context) {}
