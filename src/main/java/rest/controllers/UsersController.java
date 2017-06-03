package rest.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.JsonSyntaxException;

import exceptions.ProductListNotExistException;
import exceptions.PurchaseRecordNotExistException;
import exceptions.SelectedProductNotExistException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import model.users.User;
import rest.dtos.ProfileDTO;
import rest.dtos.ResponseDTO;
import rest.dtos.UserDTO;
import services.general.GeneralService;
import util.Address;

@Path("/users")
public class UsersController {
	
	private GeneralService generalService;
	private ResponseDTO responseDTO;

	public GeneralService getGeneralService() {
		return generalService;
	}

	public void setGeneralService(GeneralService generalService) {
		this.generalService = generalService;
	}
	
	@PostConstruct
	public void init(){
		responseDTO = new ResponseDTO();
	}
	
	// DEFAULT USERS ACTIVITY

	@GET
	@Path("/")
	@Produces("application/json")
	public Response users(){
		return responseDTO.ok(getGeneralService().getUsers());
	}
	
	@POST
	@Path("/")
	@Consumes("application/json")
	public Response createUser(String userJson){
		try {
			User user = responseDTO.gson.fromJson(userJson, User.class);
			generalService.createUser(user);
			return responseDTO.ok(user);
		} catch (JsonSyntaxException | UserAlreadyExistsException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}		
	}
	@DELETE
	@Path("/")
	public Response deleteUsers(){
		generalService.getUserService().deleteAll();
		return responseDTO.ok("users deleted");
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	//ALL METHOD INVOLVING USERS only
	@GET
	@Path("/{userId}")
	@Produces("application/json")
	public Response getById(@PathParam("userId") Integer userId){
		try {
			return responseDTO.ok(generalService.getUserById(userId));
		} catch (UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}

	@PUT
	@Path("/{userId}")
	@Consumes("application/json")
	public Response createOrUpdateUser(@PathParam("userId") Integer userId, String userJson){
		try {
			User user = responseDTO.gson.fromJson(userJson, User.class);
			generalService.createOrUpdateUser(userId, user);
			return responseDTO.ok("user updated or created");
		} catch (JsonSyntaxException | UserAlreadyExistsException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}	
	
	@DELETE
	@Path("/{userId}")
	public Response deleteUser(@PathParam("userId") Integer userId){
		try {
			generalService.deleteUser(userId);
			return responseDTO.ok("deleted user");
		} catch (UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	/////////////////////////////////////////////////////////////////////////////////
	
	//PROFILE METHODS OF AN USER
	@GET
	@Path("/{userId}/profile")
	@Produces("application/json")
	public Response getProfileById(@PathParam("userId") Integer userId){
		try {
			return responseDTO.ok(generalService.getProfile(userId));
		} catch (UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}

	//ESTE ESTA MAL, tengo q revisarlo
	@PUT
	@Path("/{id}/profile")
	public Response createOrUpdateProfile(@PathParam("id") Integer id, ProfileDTO profile){
		User u = generalService.getUserService().findById(id);
		u.getProfile().setAddress(new Address(profile.address));
		generalService.getUserService().update(u);
		return Response.ok().build();
	}

	/////////////////////////////////////////////////////////////////////////////////
	//PURCHASE METHODS OF AN USER
	@GET
	@Path("/{userId}/purchases")
	@Produces("application/json")
	public Response getPurchases(@PathParam("userId") Integer userId){
		try {
			return responseDTO.ok(generalService.getPurchaseRecords(userId));
		} catch (UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	@GET
	@Path("{userId}/purchases/{purchaseId}")
	@Produces("application/json")
	public Response getPurchaseById(@PathParam("userId") Integer userId, @PathParam("purchaseId") Integer purchaseId){
		try {
			return responseDTO.ok(generalService.getPurchaseRecord(userId, purchaseId));
		} catch (PurchaseRecordNotExistException | UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		} catch(Exception e){
			return responseDTO.error(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////
	//ADDRESS METHODS OF AN USER
	@GET
	@Path("/{userId}/address")
	@Produces("application/json")
	public Response getAddress(@PathParam("userId") Integer userId){
		try {
			return responseDTO.ok(generalService.getUserById(userId).getProfile().getAddress());
		} catch (UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// PRODUCT LISTS METHODS OF AN USER
	@GET
	@Path("/{userId}/productlists")
	@Produces("application/json")
	public Response getProductLists(@PathParam("userId") Integer userId, @QueryParam("sortedBy") List<String> sorts){
		try {
			return responseDTO.ok(generalService.getProductLists(userId));
		} catch (UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	@GET
	@Path("/{userId}/productlists/{productlistId}")
	@Produces("application/json")
	public Response getProductListById(@PathParam("userId") Integer userId, @PathParam("productlistId") Integer productlistId){
		try {
			return responseDTO.ok(generalService.getProductListById(userId, productlistId));
		} catch (UserDoesNotExistException | ProductListNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	//SELECT PRODUCT METHODS OF A LIST OF AN USER
	@GET
	@Path("/{userId}/productlists/{listId}/selectedproducts")
	@Produces("application/json")
	public Response getSelectedProducts(@PathParam("userId") Integer userId, @PathParam("listId") Integer listId){
		try {
			return responseDTO.ok(generalService.getProductListById(userId, listId).getAllProducts());
		} catch (ProductListNotExistException | UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	@GET
	@Path("/{userId}/productlists/{listId}/selectedproducts/{selectproductId}")
	@Produces("application/json")
	public Response getSelectedProducts(@PathParam("userId") Integer userId, @PathParam("listId") Integer listId, @PathParam("selectproductId") Integer selectProductId){
		try {
			return responseDTO.ok(generalService.getProductListById(userId, listId).getSelectedProduct(selectProductId));
		} catch (ProductListNotExistException | UserDoesNotExistException | SelectedProductNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	//OFFERS APPLIED OF A LIST OF AN USER METHODS
	@GET
	@Path("/{userId}/productlists/{listId}/offers")
	@Produces("application/json")
	public Response getOffers(@PathParam("userId") Integer userId, @PathParam("listId") Integer listId){
		try {
			return responseDTO.ok(generalService.getProductListById(userId, listId).getAppliedOffers());
		} catch (ProductListNotExistException | UserDoesNotExistException e) {
			return responseDTO.error(Status.CONFLICT, e.getMessage());
		}
	}	
	
//	AUTH VALIDATIONS 
	@POST
	@Path("/users/signup")
	public Response create(UserDTO user){
		try {
			generalService.createUser(user.fullUser());
			return Response.ok().build();
		} catch (UserAlreadyExistsException e) {
			return responseDTO.error(Status.FORBIDDEN, e.getMessage());
		}
	}

	

}
