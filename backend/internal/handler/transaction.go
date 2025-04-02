package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
	"verarti/internal/domain"
	"verarti/models"
)

func (h *Handler) createTransaction(c *gin.Context) {
	var input models.Transactions
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid input body")
		return
	}

	err := h.services.Transaction.CreateTransactions(input.List)
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

func (h *Handler) getAllTransactions(c *gin.Context) {
	transactions, err := h.services.Transaction.GetAllTransactions()
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
		"transactions": transactions,
	})
}

func (h *Handler) getTransactionById(c *gin.Context) {
	transactionId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	transaction, err := h.services.Transaction.GetTransactionById(transactionId)
	if err != nil {
		var errResp *domain.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, transaction)
}

func (h *Handler) deleteTransaction(c *gin.Context) {
	transactionId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	err = h.services.Transaction.DeleteTransaction(transactionId)
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

func (h *Handler) getTransactionByDate(c *gin.Context) {
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

	transactions, err := h.services.Transaction.GetTransactionByDate(date)
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
		"transactions": transactions,
	})
}

func (h *Handler) getTransactionByDateAndMethod(c *gin.Context) {
	paymentMethod := c.Query("payment_method")
	if paymentMethod == "" {
		newErrorResponse(c, http.StatusBadRequest, "payment_method is required")
		return
	}

	err := domain.ValidatePaymentMethod(paymentMethod, h.services.Transaction.GetPaymentMethods())
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
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

	transactions, err := h.services.Transaction.GetTransactionByDateAndMethod(date, paymentMethod)
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
		"transactions": transactions,
	})
}

func (h *Handler) getTransactionByDateAndType(c *gin.Context) {
	transactionType := c.Query("transaction_type")
	if transactionType == "" {
		newErrorResponse(c, http.StatusBadRequest, "transaction_type is required")
		return
	}

	err := domain.ValidateTransactionType(transactionType, h.services.Transaction.GetTransactionTypes())
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
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

	transactions, err := h.services.Transaction.GetTransactionByDateAndType(date, transactionType)
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
		"transactions": transactions,
	})
}
