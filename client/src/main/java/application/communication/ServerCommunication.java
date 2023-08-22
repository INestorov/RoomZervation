package application.communication;

import application.entities.Facility;
import application.entities.Holiday;
import application.entities.Order;
import application.entities.Room;
import application.entities.UserManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ServerCommunication {
    public static boolean isLoggedIn = false;
    private static HttpClient client = HttpClients.custom().build();

    /**
     * Sends PUT request of given content to given uri.
     *
     * @param uri     uri
     * @param content content represented in JSON
     * @return response body
     * @throws Exception when request fails or 200 is not returned
     */
    private static String put(String uri, String content) throws Exception {
        HttpPut put = new HttpPut(uri);
        put.setHeader("Content-type", "application/json");
        StringEntity entity = new StringEntity(content);
        put.setEntity(entity);
        HttpResponse httpResponse = client.execute(put);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new Exception(String.format("PUT %s %s returned %d", uri, content, statusCode));
        }
        return EntityUtils.toString(httpResponse.getEntity());
    }

    /**
     * Sends GET request to given uri.
     *
     * @param uri uri
     * @return response body
     * @throws Exception when request fails or 200 is not returned
     */
    private static String get(String uri) throws Exception {
        HttpResponse httpResponse = client.execute(new HttpGet(uri));
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new Exception(String.format("GET %s returned %d", uri, statusCode));
        }
        return EntityUtils.toString(httpResponse.getEntity());
    }

    /**
     * Sends POST request of given content to given uri.
     *
     * @param uri     uri
     * @param content content represented in JSON
     * @return response body
     * @throws Exception when request fails or 200 is not returned
     */
    private static String post(String uri, String content) throws Exception {

        HttpPost post = new HttpPost(uri);
        post.setHeader("Content-type", "application/json");
        StringEntity entity = new StringEntity(content);
        post.setEntity(entity);
        HttpResponse httpResponse = client.execute(post);

        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            throw new Exception(String.format("POST %s %s returned %d", uri, content, statusCode));
        }
        return EntityUtils.toString(httpResponse.getEntity());
    }

    private static HttpResponse post2(String uri, String content) {
        try {
            HttpPost post = new HttpPost(uri);
            post.setHeader("Content-type", "application/json");
            StringEntity entity = new StringEntity(content);
            post.setEntity(entity);

            return client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends DELETE request to given uri.
     *
     * @param uri uri
     * @return response body
     * @throws Exception when request fails or 200 is not returned
     */
    private static String delete(String uri) throws Exception {

        HttpResponse httpResponse = client.execute(new HttpDelete(uri));
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            throw new Exception(String.format("DELETE %s returned %d", uri, statusCode));
        }

        return EntityUtils.toString(httpResponse.getEntity());
    }

    /**
     * Adds building.
     *
     * @param name        name of building
     * @param openingTime opening time of building formatted as "HH:mm"
     * @param closingTime closing time of building formatted as "HH:mm"
     * @return true if building is added, otherwise false
     */
    public static boolean addBuilding(String name, String openingTime, String closingTime) {
        try {
            if (name == null || openingTime == null || closingTime == null) {
                throw new Exception("Missing required arguments");
            }
            if (!openingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")
                || !closingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")) {
                throw new Exception("Invalid time format");
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("openingTime", openingTime + ":00");
            jsonObject.addProperty("closingTime", closingTime + ":00");
            post("https://localhost:8443/buildings/post", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add food item to restaurant.
     *
     * @param restaurantId id of the restaurant
     * @param name         name of the item to add
     * @param description  description of the item
     * @param price        price of the item
     * @param type         the type of the item
     * @return return true if item added, otherwise false
     */
    public static boolean addFoodToRes(int restaurantId, String name,
                                       String description, double price, String type) {
        try {
            if (name == null || description == null || price == 0.0) {
                throw new Exception("Missing required arguments");
            }
            JsonObject subTree = new JsonObject();
            subTree.addProperty("id", restaurantId);
            JsonObject restaurantJson =
                JsonParser.parseString(getRestaurantsById(restaurantId)).getAsJsonObject();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("restaurant", restaurantJson);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("description", description);
            jsonObject.addProperty("price", price);
            jsonObject.addProperty("type", type);
            post("https://localhost:8443/menus/post", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add bikes.
     *
     * @param buildingId the id of the building
     *                   where we add the bike
     * @return true if bike is added, otherwise false
     */
    public static boolean addBike(int buildingId) {
        try {
            JsonObject subTree = new JsonObject();
            subTree.addProperty("id", buildingId);
            JsonObject buildingJson = JsonParser.parseString(getBuildingById(buildingId))
                .getAsJsonObject();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("building", buildingJson);
            System.out.println(jsonObject);
            post("https://localhost:8443/bikes/post", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return a picture of a room.
     *
     * @param roomId - id of the chosen room
     * @return a picture of a room
     */
    public static Image getPictureRoom(Integer roomId) {
        try {
            return new Image("https://localhost:8443/rooms/image/" + roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add facilities.
     *
     * @param name the name of facility you want to add
     * @return true if facility is added, otherwise false
     */
    public static boolean addFacility(String name, List<Integer> userIds) {
        try {
            Facility facility = new Facility(userIds, name);
            String json = new Gson().toJson(facility);
            post("https://localhost:8443/facilities/post", json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns buildings.
     *
     * @return JSON array of buildings
     */
    public static String getBuildings() {
        try {
            return get("https://localhost:8443/buildings/read");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Returns bikes with building.
     *
     * @return JSON array of bikes
     */
    public static String getBikesWithBuilding() {
        try {
            return get("https://localhost:8443/bikes/read/withBuilding");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Updates building.
     *
     * @param id          id of building
     * @param name        name of building
     * @param openingTime opening time of building formatted as "HH:mm"
     * @param closingTime closing time of building formatted as "HH:mm"
     * @return true if building is updated, otherwise false
     */
    public static boolean updateBuilding(String id, String name, String openingTime,
                                         String closingTime) {
        try {
            if (id == null || name == null || openingTime == null || closingTime == null) {
                throw new Exception("Missing required arguments");
            }
            if (!openingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")
                || !closingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")) {
                throw new Exception("Invalid time format");
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("openingTime", openingTime + ":00");
            jsonObject.addProperty("closingTime", closingTime + ":00");
            put("https://localhost:8443/buildings/update/" + id, jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes building by id.
     *
     * @param id id of building
     * @return true if building is deleted, otherwise false
     */
    public static boolean deleteBuilding(int id) {
        try {
            delete("https://localhost:8443/buildings/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes building by id.
     *
     * @param id id of building
     * @return true if building is deleted, otherwise false
     */
    public static boolean deleteFood(int id) {
        try {
            delete("https://localhost:8443/menus/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Deletes bike by id.
     *
     * @param id id of bike
     * @return true if bike is deleted, otherwise false
     */
    public static boolean deleteBike(int id) {
        try {
            delete("https://localhost:8443/bikes/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete facility by id.
     *
     * @param id the id of facility you want to delete
     * @return true if facility is deleted, otherwise false
     */
    public static boolean deleteFacility(int id) {
        try {
            delete("https://localhost:8443/facilities/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete facility from room with given id.
     *
     * @param facilityId id of the facility
     * @param roomId     id of the room
     * @return true if facility was deleted
     */
    public static boolean deleteFacilityFromRoom(int facilityId, int roomId) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", roomId);
            jsonObject.addProperty("facilityId", facilityId);
            post("https://localhost:8443/rooms/delete/facility", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes building by id.
     *
     * @param id id of building
     * @return true if building is deleted, otherwise false
     */
    public static boolean deleteRestaurant(int id) {
        try {
            delete("https://localhost:8443/restaurants/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to return all late bikes.
     *
     * @return JSON array
     */
    public static String getLateBikes() {
        try {
            return get("https://localhost:8443/bikeRentals/read/late");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Adds room.
     *
     * @param buildingId id of building of room
     * @param name       name of room
     * @param size       size of room
     * @return true if room is added, otherwise false
     */
    public static boolean addRoom(int buildingId, String name, int size, String description) {
        try {
            JsonObject subTree = new JsonObject();
            subTree.addProperty("id", buildingId);
            JsonObject buildingJson = JsonParser.parseString(getBuildingById(buildingId))
                .getAsJsonObject();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("building", buildingJson);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("size", size);
            jsonObject.addProperty("description", description);
            System.out.println(jsonObject);
            post("https://localhost:8443/rooms/post", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns rooms.
     *
     * @return JSON array of rooms
     */
    public static String getRooms() {
        try {
            int minSize = 0;
            int maxSize = 10000;
            return get("https://localhost:8443/rooms/read/" + minSize + "/" + maxSize);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Gets a room with the given Id.
     * In case we will switch the filtering code to the one on the Back-End.
     *
     * @param minSize    min size requested
     * @param maxSize    max size requested
     * @param facilityId facility requested (optional)
     * @param buildingId building requested (also optional)
     * @return
     */
    public static String getFilteredRooms(int minSize, int maxSize,
                                          int facilityId, int buildingId) {
        try {
            return get("https://localhost:8443/rooms/read/" + minSize + "/" + maxSize + "/"
                + facilityId + "/" + buildingId);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }

    }

    /**
     * Gets a room with the given Id.
     *
     * @param id of the room to be retrieved
     * @return
     */
    public static String getRoomById(int id) {
        try {
            return get("https://localhost:8443/rooms/read1/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Updates room.
     *
     * @param id         id of room
     * @param buildingId id of building of room
     * @param name       name of room
     * @param size       size of room
     * @return true if room is updated, otherwise false
     */
    public static boolean updateRoom(int id, int buildingId, String name, int size,
                                     String description) {
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject subTree = new JsonObject();
            subTree.addProperty("id", buildingId);
            JsonObject buildingJson = JsonParser.parseString(getBuildingById(buildingId))
                .getAsJsonObject();
            jsonObject.add("building", buildingJson);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("size", size);
            jsonObject.addProperty("description", description);
            put("https://localhost:8443/rooms/update/" + id, jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method that updates bike location.
     *
     * @param id         id of bike
     * @param buildingId id of building
     * @return true if bike is updated, otherwise false
     */
    public static boolean updateBike(int id, int buildingId) {
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject subTree = new JsonObject();
            subTree.addProperty("id", buildingId);
            JsonObject buildingJson = JsonParser.parseString(getBuildingById(buildingId))
                .getAsJsonObject();
            jsonObject.add("building", buildingJson);
            put("https://localhost:8443/bikes/update/" + id, jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes room by id.
     *
     * @param id id of room
     * @return true if room is deleted, otherwise false
     */
    public static boolean deleteRoom(int id) {
        try {
            delete("https://localhost:8443/rooms/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns reservation by id.
     *
     * @param id id of reservation
     * @return JSON object of reservation
     */
    public static String getReservation(int id) {
        try {
            return get("https://localhost:8443/reservations/read/id/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }


    /**
     * Retrieves a building with the given Id.
     *
     * @param id of the building to be retrieved
     * @return
     */
    public static String getBuildingById(int id) {
        try {
            return get("https://localhost:8443/buildings/read/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    /**
     * Retrieves facilities by room.
     *
     * @param id room id
     * @return json array of facilities.
     */
    public static String getFacilityByRoom(int id) {
        try {
            return get("https://localhost:8443/rooms/read/facilities/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    /**
     * Retrieves facilities.
     *
     * @return json array of facilities.
     */
    public static String getFacility() {
        try {
            return get("https://localhost:8443/facilities/read");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Returns reservations.
     *
     * @return JSON array of reservations
     */
    public static String getReservations() {
        try {
            return get("https://localhost:8443/reservations/read");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Deletes reservation by id.
     *
     * @param id id of reservation
     * @return true if reservation is deleted, otherwise false
     */
    public static boolean deleteReservation(int id) {
        try {
            delete("https://localhost:8443/reservations/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes user by id.
     *
     * @param id id of user
     * @return true if user is deleted, otherwise false
     */
    public static boolean deleteUser(int id) {
        try {
            delete("https://localhost:8443/users/delete/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get request find all bikes in database.
     *
     * @return String in json format containing all bikes
     */
    public static String getAllBikes() {
        try {
            return get("https://localhost:8443/bikes/read");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Find number of bikes located ad building with specific id.
     *
     * @param id Id of the building
     * @return the number of bikes located at specified building as String
     */
    public static String getAvailableBikesNumberByBuildingId(int id) {
        try {
            return get("https://localhost:8443/bikes/read/number/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * Get all bikes at building with given Id.
     *
     * @param id Id of the building
     * @return All bikes at building with given id
     */
    public static String getBikesByBuildingId(int id) {
        try {
            return get("https://localhost:8443/bikes/read/building/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Sends a get request for activating the password recovery procedure.
     */
    public static String recovPass(String to) {
        try {
            return get("https://localhost:8443/mail/send/" + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    /**
     * Add a new reservation requested by the client.
     *
     * @param room       room to add
     * @param endValue   end time of the reservation
     * @param startValue start time of the reservation
     * @return
     */
    public static int addReservation(Room room, String startValue, String endValue,
                                     String username) {
        try {
            endValue = endValue.replace(".", ":");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("roomId", room.getId());
            jsonObject.addProperty("startTime", startValue);
            jsonObject.addProperty("endTime", endValue);
            String id = getUserId1(username);
            if (!id.equals("Not found")) {
                int userId = Integer.parseInt(id);
                jsonObject.addProperty("userId", userId);
                System.out.println(jsonObject);
                String s = post("https://localhost:8443/reservations/post", jsonObject.toString());
                if (s.equals("true")) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Reservation failed. Please restart the app");
                alert.showAndWait();
                return -1;
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Reservation failed. Please restart the app");
            alert.showAndWait();
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Returns the type of user.
     *
     * @param username param used
     * @return the type of user
     */
    public static String getUserType(String username) {
        try {
            return get("https://localhost:8443/users/type/" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User not found";
    }

    /**
     * Returns the reservations made by a user.
     *
     * @param username username of the user
     * @return reservations made by user
     */
    public static String getReservationByUsername(String username) {
        try {
            return get("https://localhost:8443/reservations/read1/" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No reservation found";
    }

    /**
     * Logs in with given username and password.
     *
     * @param username username
     * @param password password
     * @return {@link JsonObject} or null
     */
    public static JsonObject login(String username, String password) {
        HttpPost httpPost = new HttpPost("https://localhost:8443/login");
        httpPost.setEntity(new UrlEncodedFormEntity(
            List.of(new BasicNameValuePair("username", username),
                new BasicNameValuePair("password", password)), Consts.UTF_8));
        try {
            return client.execute(httpPost, response -> {
                isLoggedIn = response.getStatusLine().getStatusCode() == 200;
                return Optional.ofNullable(response.getEntity()).map(httpEntity -> {
                    try {
                        return JsonParser.parseString(EntityUtils.toString(httpEntity))
                            .getAsJsonObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).orElse(null);
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logs out.
     */
    public static void logout() {
        isLoggedIn = false;
        try {
            client.execute(new HttpGet("https://localhost:8443/logout"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a POST request to /register.
     *
     * @param id       id
     * @param username username
     * @param password password
     * @return {@link JsonObject} containing error message or null if registration was successful
     */
    public static JsonObject register(int id, String username, String password, String phoneNumber,
                                      String mail) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("phoneNumber", phoneNumber);
        jsonObject.addProperty("email", mail);
        try {
            HttpPost httpPost = new HttpPost("https://localhost:8443/register");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            return client.execute(httpPost,
                response -> response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED
                    ? null :
                    Optional.ofNullable(response.getEntity()).map(httpEntity -> {
                        try {
                            return JsonParser.parseString(EntityUtils.toString(httpEntity))
                                .getAsJsonObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return JsonParser.parseString(String
                                .format("{\"error\":{\"code\":%d,\"message\":\"%s\"}}",
                                    HttpStatus.SC_BAD_REQUEST, e.getMessage())).getAsJsonObject();
                        }
                    }).orElseGet(() -> JsonParser.parseString(String
                        .format("{\"error\":{\"code\":%d,\"message\":\"%s\"}}",
                            HttpStatus.SC_BAD_REQUEST, "Unknown error")).getAsJsonObject()));
        } catch (IOException e) {
            e.printStackTrace();
            return JsonParser.parseString(String
                .format("{\"error\":{\"code\":%d,\"message\":\"%s\"}}", HttpStatus.SC_BAD_REQUEST,
                    e.getMessage())).getAsJsonObject();
        }
    }


    /**
     * Send get request to get user id of given username.
     * user id is converted to int.
     *
     * @param username Username
     * @return int userId
     */
    public static int getUserId(String username) {
        try {
            return Integer.parseInt(get("https://localhost:8443/users/id/" + username));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Send get request to get user id of given username.
     * user id is converted to int.
     *
     * @param username Username
     * @return int userId
     */
    public static String getUserId1(String username) {
        try {
            return get("https://localhost:8443/users/id/" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not found";
    }

    /**
     * Method triggered by rent bike button.
     * Send post request to rent bike.
     *
     * @param bikeId id of the bike to rent
     * @param userId id of the user
     * @return JsonObject containing status and message
     */
    public static JsonObject rentBike(int bikeId, int userId, long date) {
        JsonObject jsonObject = new JsonObject();
        try {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("userId", userId);
            jsonObject1.addProperty("bikeId", bikeId);
            jsonObject1.addProperty("Date", date);
            HttpResponse httpResponse = post2("https://localhost:8443/bikes/rent", jsonObject1.toString());
            System.out.println(jsonObject1.toString());
            jsonObject.addProperty("status", httpResponse.getStatusLine().getStatusCode());
            jsonObject.addProperty("message", EntityUtils.toString(httpResponse.getEntity()));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", 400);
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject;
        }
    }

    /**
     * Pick up the bike from the current reservation.
     *
     * @return JsonObject containing status and message.
     */
    public static JsonObject pickUpBike() {
        JsonObject jsonObject = new JsonObject();
        JsonObject contentJson = new JsonObject();
        contentJson.addProperty("id", UserManager.getInstance().getUser().getId());
        try {
            HttpResponse httpResponse = post2("https://localhost:8443/bikes/pickup", contentJson.toString());
            jsonObject.addProperty("status", httpResponse.getStatusLine().getStatusCode());
            jsonObject.addProperty("message", EntityUtils.toString(httpResponse.getEntity()));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", 400);
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject;
        }
    }

    /**
     * Send post to server to cancel current reservation.
     *
     * @return JsonObject containing status and message from server.
     */
    public static JsonObject cancelBike() {
        JsonObject jsonObject = new JsonObject();
        JsonObject contentJson = new JsonObject();
        contentJson.addProperty("id", UserManager.getInstance().getUser().getId());
        try {
            HttpResponse httpResponse = post2(
                "https://localhost:8443/bikes/cancel", contentJson.toString());
            jsonObject.addProperty("status", httpResponse.getStatusLine().getStatusCode());
            jsonObject.addProperty("message", EntityUtils.toString(httpResponse.getEntity()));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", 400);
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject;
        }
    }

    /**
     * Method to check if current user has bike in use.
     *
     * @return boolean is bike in use by given user
     */
    public static boolean isBikeInUse() {
        boolean isInUse = false;
        try {
            isInUse = Boolean.parseBoolean(get("https://localhost:8443/bikes/read/isUser/" + UserManager.getInstance().getUser().getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInUse;
    }

    /**
     * get bike by user id.
     *
     * @return String in json format
     */
    public static String getBikeByUserId() {
        try {
            return get("https://localhost:8443/bikes/read/user/" + UserManager.getInstance().getUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Send post to server to find the bike that is linked to user.
     * Set user of that bike to null.
     *
     * @return
     */
    public static JsonObject newBikeReservation() {
        JsonObject jsonObject = new JsonObject();
        try {
            JsonObject contentJson = new JsonObject();
            contentJson.addProperty("userId", UserManager.getInstance().getUser().getId());
            HttpResponse httpResponse = post2("https://localhost:8443/bikes/read/user/", contentJson.toString());
            jsonObject.addProperty("status", httpResponse.getStatusLine().getStatusCode());
            jsonObject.addProperty("message", EntityUtils.toString(httpResponse.getEntity()));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", 400);
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject;
        }
    }

    /**
     * Method triggered by return bike button to return bike..
     *
     * @param bikeId     id of the bike
     * @param buildingId id of the building where the bike is returned
     * @return true if bike was correctly returned
     */
    public static boolean returnBike(int bikeId, int buildingId) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("bikeId", bikeId);
            jsonObject.addProperty("buildingId", buildingId);
            String s = post("https://localhost:8443/bikes/update/return", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send get request to server to get all menus from restaurant with given id.
     *
     * @param restaurantId id of the restaurant
     * @return String in json format containing all menus
     */
    public static String getFoodByRestaurant(int restaurantId) {
        try {
            return get("https://localhost:8443/menus/read/all/" + restaurantId);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Add food item to restaurant.
     *
     * @param restaurantId id of the restaurant
     * @param name         name of the item to add
     * @param description  description of the item
     * @param price        price of the item
     * @param type         the type of the item
     * @return return true if item added, otherwise false
     */
    public static boolean updateFood(int id, int restaurantId, String name,
                                     String description, String type, double price) {
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject subTree = new JsonObject();
            subTree.addProperty("id", restaurantId);
            JsonObject restaurantJson = JsonParser.parseString(getRestaurantsById(restaurantId))
                .getAsJsonObject();
            jsonObject.add("restaurant", restaurantJson);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("description", description);
            jsonObject.addProperty("type", type);
            jsonObject.addProperty("price", price);
            put("https://localhost:8443/menus/update/" + id, jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates restaurant.
     *
     * @param id          id of building
     * @param name        name of building
     * @param openingTime opening time of building formatted as "HH:mm"
     * @param closingTime closing time of building formatted as "HH:mm"
     * @return true if building is updated, otherwise false
     */
    public static boolean updateRestaurant(String id, String name, String openingTime,
                                           String closingTime, int buildingId) {
        try {
            if (id == null || name == null || openingTime == null || closingTime == null) {
                throw new Exception("Missing required arguments");
            }
            if (!openingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")
                || !closingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")) {
                throw new Exception("Invalid time format");
            }
            JsonObject jsonObject = new JsonObject();
            JsonObject buildingJson = JsonParser.parseString(getBuildingById(buildingId))
                .getAsJsonObject();
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("openingTime", openingTime + ":00");
            jsonObject.addProperty("closingTime", closingTime + ":00");
            jsonObject.add("building", buildingJson);
            put("https://localhost:8443/restaurants/update/" + id, jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all restaurants from server.
     *
     * @return String in json format containing all restaurants
     */
    public static String getRestaurants() {
        try {
            return get("https://localhost:8443/restaurants/read");
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Get all restaurants by id.
     *
     * @param id id of restaurant
     * @return a string in json format with the restaurant
     */
    public static String getRestaurantsById(int id) {
        try {
            return get("https://localhost:8443/restaurants/read/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Send get request to server to get menu with given id.
     *
     * @param id Id of the menu
     * @return String in Json format containing menu with given id* */
    public static String getFoodById(int id) {
        try {
            return get("https://localhost:8443/menus/read/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Send Post request to server to create a new order.
     *
     * @param order Order containing id's of all items in basket,
     *              reservation id, restaurant id
     *              and the time at which it was ordered
     * @return jsonObject containing server response
     */
    public static JsonObject placeOrder(Order order) {
        JsonObject jsonObject = new JsonObject();
        try {
            Gson gson = new Gson();
            String s = gson.toJson(order);
            HttpResponse httpResponse = post2("https://localhost:8443/orders/post", s);
            jsonObject.addProperty("status", httpResponse.getStatusLine().getStatusCode());
            jsonObject.addProperty("message", EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", 400);
            jsonObject.addProperty("message", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Send get request to server to get all orders.
     *
     * @return String in jsonArray format containing all orders.
     */
    public static String getOrders(String username) {
        try {
            return get("https://localhost:8443/orders/user/" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    /**
     * Send get request to server to get order by given id.
     *
     * @param id Id of the order.
     * @return String in json format containing the given order.
     */
    public static String getOrderById(int id) {
        try {
            return get("https://localhost:8443/orders/read/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * Returns the reservations made by a user.
     *
     * @param username username of the user
     * @return reservations made by user
     */
    public static String getReservationSpecific(String username) {
        try {
            return get("https://localhost:8443/reservations/read/" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No reservation found";
    }

    /**
     * Add restaurant.
     *
     * @param buildingId  the id of the building
     * @param name        the name of the restaurant
     * @param openingTime opening time
     * @param closingTime closing time
     * @return return true if restaurant added, otherwise false
     */
    public static boolean addRestaurant(int buildingId, String name,
                                        String openingTime, String closingTime) {
        try {
            if (name == null || openingTime == null || closingTime == null) {
                throw new Exception("Missing required arguments");
            }
            if (!openingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")
                || !closingTime.matches("([0-1]\\d|2[0-3]):[0-5]\\d")) {
                throw new Exception("Invalid time format");
            }
            JsonObject jsonObject = new JsonObject();
            JsonObject buildingJson =
                JsonParser.parseString(getBuildingById(buildingId)).getAsJsonObject();
            jsonObject.add("building", buildingJson);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("openingTime", openingTime + ":00");
            jsonObject.addProperty("closingTime", closingTime + ":00");
            post("https://localhost:8443/restaurants/post", jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method for checking the activation key.
     *
     * @param key key entered by user
     * @return string to check password
     */
    public static String checkKey(String key) {
        try {
            return get("https://localhost:8443/mail/check/" + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    /**
     * Method for changing the password.
     *
     * @param username of user that requested a password change
     * @param text     new password
     * @return string of new password
     */
    public static String changePassword(String username, String text) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", username);
            jsonObject.addProperty("password", text);
            return put("https://localhost:8443/update/password", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    /**
     * Method to get all holidays.
     *
     * @return holidays
     */
    public static String getHolidays() {
        try {
            return get("https://localhost:8443/holidays/read");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";

    }

    /**
     * Method for changing user privilege.
     *
     * @param userID user id of user
     * @param text   type of privilege
     * @return boolean type
     * @throws Exception exception
     */
    public static String authorizeUser(int userID, String text) throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userID);
        jsonObject.addProperty("privilege", text);
        return put("https://localhost:8443/users/upgrade", jsonObject.toString());
    }


    /**
     * Set httpClient to a new one.
     *
     * @param client - new httpClient
     */
    public static void setClient(HttpClient client) {
        ServerCommunication.client = client;
    }

    /**
     * Send post to server to add new Holiday to the database.
     *
     * @param holiday Holiday to add to the database
     * @return JsonObject containing status and message
     */
    public static JsonObject addHoliday(Holiday holiday) {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("name", holiday.getName());
        requestJson.addProperty("start", holiday.getStart().toString());
        requestJson.addProperty("end", holiday.getEnd().toString());
        requestJson.addProperty("repetitive", holiday.isRepetitive());
        JsonObject jsonObject = new JsonObject();
        System.out.println(requestJson.toString());
        try {
            HttpResponse httpResponse = post2("https://localhost:8443/holidays/create", requestJson.toString());
            jsonObject.addProperty("status", httpResponse.getStatusLine().getStatusCode());
            jsonObject.addProperty("message", EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", 400);
            jsonObject.addProperty("message", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Send delete to server to delete holiday with given id.
     *
     * @param id Id of the holiday
     * @return true if deletion was successful
     */
    public static boolean deleteHoliday(int id) {
        try {
            delete("https://localhost:8443/holidays/delete/" + id);
            return true;
        } catch (Exception e) {
            return false;

        }
    }
}

