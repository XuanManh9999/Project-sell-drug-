package com.back_end.myProject.controller;

import com.back_end.myProject.dto.BillDTO;
import com.back_end.myProject.entities.Bill;
import com.back_end.myProject.entities.Category;
import com.back_end.myProject.service.IBill;
import com.back_end.myProject.utils.ResponseCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class BillController {
    private final IBill billService;

    public BillController(IBill billService) {
        this.billService = billService;
    }


    @GetMapping(value = "/bills")
    public ResponseEntity<?> getAllBills(Pageable pageable) {
        ResponseCustom responseCustom;
        try {
            Page<Bill> bills = billService.getAllBills(pageable);
            responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Get All Bills", bills);
            return new ResponseEntity<>(responseCustom, HttpStatus.OK);
        }catch (Exception ex) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "/bill")
    public ResponseEntity<?> addBill(@RequestBody BillDTO billDTO) {
        ResponseCustom responseCustom;
        try {
            String name = billDTO.getName();
            String customer_phone = billDTO.getCustomer_phone();
            String customer_name = billDTO.getCustomer_name();
            // Kiểm tra null hoặc chuỗi rỗng
            if (name == null || name.isEmpty() || customer_name == null || customer_name.isEmpty() || customer_phone == null || customer_phone.isEmpty()) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Bad Request: Missing or empty fields", HttpStatus.BAD_REQUEST.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
            }

            Boolean isCreate = billService.creayeBill(billDTO);
            if (isCreate) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Bill created successfully", HttpStatus.OK.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            } else {
                responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), "Failed to create bill: Conflict", HttpStatus.CONFLICT.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.CONFLICT);
            }
        }catch (Exception ex) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping(value = "/bill")
    public ResponseEntity<?> updateBill(@RequestBody BillDTO billDTO) {
        ResponseCustom responseCustom;
        try {
            Long id = billDTO.getId();
            String name = billDTO.getName();
            String customer_phone = billDTO.getCustomer_phone();
            String customer_name = billDTO.getCustomer_name();
            if (id == null || id <= 0 || name == null || name.isEmpty() || customer_name == null || customer_name.isEmpty() || customer_phone == null || customer_phone.isEmpty()) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Bad Request: Missing or empty fields", HttpStatus.BAD_REQUEST.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
            }
            Boolean isUpdate = billService.updateBill(billDTO);
            if (isUpdate) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Bill updated successfully", HttpStatus.OK.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            }
            responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), "Bill updated CONFLICT", HttpStatus.OK.getReasonPhrase());
            return new ResponseEntity<>(responseCustom, HttpStatus.CONFLICT);
        }catch (Exception ex) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/bill/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {
        ResponseCustom responseCustom;
        try {
            if (id == null || id <= 0) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Bad Request: Invalid id", HttpStatus.BAD_REQUEST.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
            }
            Boolean isDelete = billService.deleteBill(id);
            if (isDelete) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Bill deleted successfully", HttpStatus.OK.getReasonPhrase());
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            }
            responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), "Bill deleted CONFLICT", HttpStatus.CONFLICT.getReasonPhrase());
            return new ResponseEntity<>(responseCustom, HttpStatus.CONFLICT);
        }catch (Exception ex) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
