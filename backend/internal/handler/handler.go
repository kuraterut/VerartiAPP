package handler

import (
	"github.com/gin-gonic/gin"
	"verarti/internal/service"
)

type Handler struct {
	services *service.Service
}

func NewHandler(services *service.Service) *Handler {
	return &Handler{services: services}
}

func (h *Handler) InitRoutes() *gin.Engine {
	router := gin.New()

	auth := router.Group("/auth")
	{
		auth.POST("/signin", h.signIn)
	}

	api := router.Group("/api", h.userIdentity)
	{
		master := api.Group("/master", h.masterIdentity)
		{
			master.GET("/token")

			profile := master.Group("/profile")
			{
				profile.GET("/", h.getUserInfo)
				profile.PUT("/photo", h.updatePhoto)
				profile.PUT("/info", h.updateInfo)
				profile.PUT("/password", h.updatePassword)
			}

			appointment := master.Group("/appointment")
			{
				appointment.GET("/", h.GetAppointmentsForMasterByDate)
			}
		}

		admin := api.Group("/admin", h.adminIdentity)
		{
			users := admin.Group("/users")
			{
				users.POST("/signup", h.signUp)
				users.GET("/master", h.getAllMasters)
				users.GET("/master/:id", h.getMasterById)
				users.GET("/admin", h.getAllAdmins)
				users.GET("/admin/:id", h.getAdminById)
				users.GET("/phone", h.getUserByPhone)
				users.DELETE("/:id", h.deleteUser)
			}

			clients := admin.Group("/clients")
			{
				clients.POST("/", h.createClient)
				clients.GET("/", h.getAllClients)
				clients.GET("/:id", h.getClientById)
				clients.GET("/phone", h.getClientByPhone)
				clients.PUT("/:id", h.updateClient)
			}

			product := admin.Group("/product")
			{
				product.POST("/", h.createProduct)
				product.GET("/", h.getAllProducts)
				product.GET("/:id", h.getProductById)
				product.PUT("/:id", h.updateProduct)
				product.DELETE("/:id", h.deleteProduct)
			}

			option := admin.Group("/option")
			{
				option.POST("/", h.createOption)
				option.GET("/", h.getAllOptions)
				option.GET("/:id", h.getOptionById)
				option.PUT("/:id", h.updateOption)
				option.DELETE("/:id", h.deleteOption)

				option.GET("/master", h.getOptionsByMasterId)
				option.POST("/:id", h.addOptionForMaster)
				option.DELETE("/master/:id", h.removeOptionFromTheMaster)
			}

			appointment := admin.Group("/appointment")
			{
				appointment.POST("/", h.createAppointment)
				appointment.DELETE("/:id", h.deleteAppointmentById)
				appointment.PUT("/:id", h.updateAppointmentById)
				appointment.GET("/:id", h.getAppointmentById)

				appointment.GET("/date", h.getAllAppointmentsByDate)
				appointment.GET("/client/:id", h.getAppointmentByClientId)

				appointment.POST("/admin", h.putAdminToDate)
				appointment.POST("/master", h.putMasterToDate)
				appointment.DELETE("/master", h.cancelMasterEntryForDate)
				appointment.GET("/admin", h.getAdminByDate)
				appointment.GET("/master", h.getAllMastersByDate)

				appointment.GET("/schedule", h.getMonthlySchedule)
			}

			transaction := admin.Group("/transaction")
			{
				transaction.POST("/", h.createTransaction)
				transaction.GET("/", h.getAllTransactions)
				transaction.GET("/:id", h.getTransactionById)
				transaction.GET("/date-only", h.getTransactionByDate)
				transaction.GET("/method", h.getTransactionByDateAndMethod)
				transaction.GET("/type", h.getTransactionByDateAndType)
				transaction.DELETE("/:id", h.deleteTransaction)
			}
		}

	}

	return router
}
