package application.controllers;

import application.entities.Food;
import application.entities.Order;
import application.entities.OrderItem;
import application.entities.OrderRequestBody;
import application.entities.Reservation;
import application.repositories.FoodRepository;
import application.repositories.OrderItemRepository;
import application.repositories.OrderRepository;
import application.repositories.ReservationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderRepository repository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    /**
     * GET endpoint to read all orders.
     *
     * @return all orders
     */
    @GetMapping("/read")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint to read an order by its id.
     *
     * @return order requested
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No order exists");
        }
    }

    /**
     * POST Endpoint to create an order.
     *
     * @return list of orders
     */
    @PostMapping("/post")
    public Order create(@RequestBody final OrderRequestBody order) {

        Order newEntity = new Order();
        Optional<Reservation> reservation = reservationRepository
            .findById(order.getReservationId());
        Reservation reservationEntity = reservation.get();
        newEntity.setReservation(reservationEntity);
        if (reservationEntity.getStart().after(order.getDate())) {
            newEntity.setDate(reservationEntity.getStart());
        } else {
            newEntity.setDate(order.getDate());
        }
        List<Integer> foodIds = order.getFoodIds();
        repository.save(newEntity);
        for (int i = 0; i < foodIds.size(); i++) {
            Optional<OrderItem> orderItem = orderItemRepository
                .findOrderItemsByFood_IdAndOrder_Id(
                    foodIds.get(i), newEntity.getId()
                );
            if (orderItem.isPresent()) {
                OrderItem orderItemEntity = orderItem.get();
                orderItemEntity.addAmount();
                orderItemRepository.save(orderItemEntity);
            } else {
                Optional<Food> food = foodRepository.findById(foodIds.get(i));
                OrderItem orderItemEntity = new OrderItem(food.get(), newEntity);
                newEntity.getFoods().add(orderItemEntity);
                orderItemRepository.save(orderItemEntity);
            }
        }
        repository.save(newEntity);
        return newEntity;
    }


    /**
     * PUT endpoint to update an order.
     *
     * @param id     - id of the order to modify
     * @param entity - Order to modify
     * @return list of orders
     * @throws IllegalArgumentException if order doesn't exist
     */
    @PutMapping("update/{id}")
    public List<Order> updateOrderById(
        @PathVariable("id") Integer id, @RequestBody final Order entity)
        throws IllegalArgumentException {
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            System.out.println(order.toString());
            Order newEntity = order.get();

            newEntity.setReservation(entity.getReservation());
            newEntity.setFoods(entity.getFoods());
            newEntity.setDate(entity.getDate());

            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No order exists");
        }
    }


    /**
     * DELETE Endpoint to delete an order by its id.
     *
     * @param id - id of the order to delete.
     * @return list of order
     * @throws IllegalArgumentException if order was not found
     */
    @DeleteMapping("delete/{id}")
    public List<Order> deleteOrderById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Order> order = repository.findById(id);

        if (order.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No order exists");
        }
    }

    /**
     * POST endpoint to add food to an order.
     *
     * @param food - food to add to order
     * @param id   - id of the order where to add new food.
     * @return order where food was added
     * @throws IllegalArgumentException if order does not exist
     */
    @PostMapping("/add/food")
    public Optional<Order> addFood(@RequestBody final Food food,
                                   @RequestParam("orderId") Integer id)
        throws IllegalArgumentException {

        Optional<Order> order = repository.findById(id);

        if (order.isPresent()) {
            Order newEntity = order.get();
            Optional<OrderItem> orderItem = orderItemRepository
                .findOrderItemsByFood_IdAndOrder_Id(id, food.getId());
            if (orderItem.isPresent()) {
                OrderItem orderItemEntity = orderItem.get();
                newEntity.getFoods().remove(orderItemEntity);
                orderItemEntity.addAmount();
                newEntity.getFoods().add(orderItemEntity);
                orderItemRepository.save(orderItemEntity);
            } else {
                OrderItem orderItemEntity = new OrderItem(food, newEntity);
                newEntity.getFoods().add(orderItemEntity);
                orderItemRepository.save(orderItemEntity);
            }
            repository.save(newEntity);
            return repository.findById(id);
        } else {
            throw new IllegalArgumentException("No order found");
        }
    }

    /**
     * DELETE endpoint to remove food from an order.
     *
     * @param id   - id of the order where to remove the food
     * @param food -  food to remove from a order
     * @return order where food was removed
     * @throws IllegalArgumentException if order or food do not exist
     */
    @Cascade(CascadeType.DELETE)
    @DeleteMapping("/delete/food")
    public Optional<Order> deleteFoodFromOrder(
        @RequestParam("orderId") Integer id, @RequestBody Food food)
        throws IllegalArgumentException {

        Optional<Order> order = repository.findById(id);

        if (order.isPresent()) {
            Order newEntity = order.get();
            int foodId = food.getId();
            Optional<OrderItem> orderItem = orderItemRepository
                .findOrderItemsByFood_IdAndOrder_Id(foodId, id);
            if (newEntity.getFoods().size() > 0
                && orderItem.isPresent()) {
                OrderItem orderItemEntity = orderItem.get();
                if (orderItemEntity.getAmount() == 1) {
                    orderItemRepository.deleteById(orderItemEntity.getId());
                    newEntity.getFoods().remove(orderItemEntity);
                } else {
                    orderItemEntity.subtractAmount();
                }
                return repository.findById(id);
            } else {
                throw new IllegalArgumentException("order has no such food");
            }
        } else {
            throw new IllegalArgumentException("No order exist");
        }
    }

    /**
     * get all orders from 1 user.
     *
     * @param username the username of user
     * @return orders
     * @throws IllegalArgumentException no orders found
     */
    @GetMapping("user/{username}")
    public ResponseEntity<List<Order>> findOrdersByUser(@PathVariable String username)
        throws IllegalArgumentException {
        List<Order> list = repository.findOrdersByReservation_User_Username(username);
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("no orders for user");
        }
    }


}
