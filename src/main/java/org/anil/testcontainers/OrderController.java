package org.anil.testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Order;
import org.anil.testcontainers.entity.OrderEntity;
import org.anil.testcontainers.repository.OrderEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    OrderEntityRepository orderEntityRepository;

    @PostMapping("/save")
    public ResponseEntity<OrderEntity> saveOrderEntity(@RequestBody OrderEntity orderEntityDto){
       OrderEntity orderEntity = new OrderEntity();
       orderEntity.setOrderName(orderEntityDto.getOrderName());
       orderEntity.setOrderType(orderEntityDto.getOrderType());
        orderEntityRepository.save(orderEntity);
       return ResponseEntity.ok(orderEntity);
    }

    @GetMapping("/allorders")
    public ResponseEntity<List<OrderEntity>> saveOrderEntity(){
        var query  =entityManager.createNativeQuery("""
                    select id, 
                    order_name,
                    order_type,
                    if(order_type = '1','FullFilled','Cancelled') from order_entity ;
            """);
       var resultList = (List<Object[]>) query.getResultList();
       List<OrderEntity> list = new ArrayList<>();
       for(Object[] object : resultList){
           var id = Long.valueOf(object[0].toString());
           var orderName = object[1].toString();
           var orderType = Long.valueOf(object[2].toString());
           var orderTypeDesc = object[3].toString();
           OrderEntity orderEntity = new OrderEntity();
           orderEntity.setId(id);
           orderEntity.setOrderTypeDes(orderTypeDesc);
           orderEntity.setOrderType(orderType);
           list.add(orderEntity);
       }
       return ResponseEntity.ok(list);
    }

}
