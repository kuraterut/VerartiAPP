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
			profile := master.Group("/profile")
			{
				profile.GET("/", h.getUserInfo)
				profile.PUT("/photo", h.updatePhoto)
				profile.PUT("/info", h.updateInfo)
				profile.PUT("/password", h.updatePassword)
			}

			resource := master.Group("/resource")
			{
				resource.GET("/", h.getResourcesByMasterId)
				resource.GET("/all", h.getAllResources)
				resource.GET("/:id", h.getResourceById)
				resource.POST("/:id", h.addResource)
				resource.POST("/request", h.createRequest)
				resource.GET("/request", h.getRequests)
				resource.GET("/response", h.getResponseByRequestId)
			}

			appointment := master.Group("/appointment")
			{
				appointment.GET("/admin", h.getAdminByDate)
				appointment.GET("/master", h.getAllMastersByDate)

				appointment.GET("/day", h.getDailyAppointment)
				appointment.GET("/month", h.getMonthlyAppointment)
				appointment.POST("/request", h.cancellationRequest)
			}

			option := master.Group("/option")
			{
				option.GET("/", h.getAllOptions)
				option.GET("/:id", h.getOptionById)
			}

			users := master.Group("/users")
			{
				users.GET("/master", h.getAllMasters)
				users.GET("/master/:id", h.getMasterById)
				users.GET("/admin", h.getAllAdmins)
				users.GET("/admin/:id", h.getAdminById)
			}

			clients := master.Group("/clients")
			{
				clients.GET("/", h.getAllClients)
				clients.GET("/:id", h.getClientById)
				clients.GET("/phone", h.getClientByPhone)
			}

			feedback := master.Group("/feedback")
			{
				feedback.GET("/", h.getAllFeedbacks)
				feedback.GET("/:id", h.getFeedbackById)
				feedback.PATCH("/", h.sortFeedbacks) // todo не помню зачем это нужно, мб поменять тип запроса
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

			feedback := admin.Group("/feedback")
			{
				feedback.GET("/")
				feedback.GET("/:id")
				feedback.POST("/")
			}

			resource := admin.Group("/resource")
			{
				resource.POST("/", h.createResource)
				resource.GET("/", h.getAllResources)
				resource.GET("/:id", h.getResourceById)
				//resource.POST("/response", h.createResponse) // todo
				//resource.GET("/request", h.getRequests)
				//resource.GET("/response", h.getResponseByRequestId)
			}

			option := admin.Group("/option")
			{
				option.POST("/", h.createOption)
				option.POST("/:id", h.addOptionForMaster)
				option.GET("/", h.getAllOptions)
				option.GET("/master", h.getOptionsByMasterId)
				option.GET("/:id", h.getOptionById)
				option.PUT("/:id", h.updateOption)
				option.DELETE("/:id", h.deleteOption)
			}

			appointment := admin.Group("/appointment")
			{
				appointment.POST("/", h.createAppointment)
				appointment.GET("/date", h.getAllAppointmentsByDate)
				appointment.GET("/client/:id", h.getAppointmentByClientId)

				appointment.POST("/admin", h.putAdminToDate)
				appointment.POST("/master", h.putMasterToDate)
				appointment.GET("/admin", h.getAdminByDate)
				appointment.GET("/master", h.getAllMastersByDate)

				appointment.GET("/day", h.getDailyAppointment)
				appointment.GET("/month", h.getMonthlyAppointment)
				appointment.POST("/request", h.cancellationRequest)
			}
		}

	}

	return router
}
