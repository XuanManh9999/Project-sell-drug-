package com.back_end.myProject.controller;

import com.back_end.myProject.dto.MedicineDTO;
import com.back_end.myProject.service.IMedicine;
import com.back_end.myProject.utils.ResponseCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MedicineController {

    private final IMedicine IMedicineService;

    @Autowired
    public MedicineController(IMedicine iMedicine) {
        this.IMedicineService = iMedicine;
    }

    // Lấy danh sách thuốc (phân trang)
    @GetMapping("/medicines")
    public ResponseEntity<?> medicines(Pageable pageable) {
        ResponseCustom responseCustom;
        try {
            Page<MedicineDTO> medicines = IMedicineService.getAllMedicines(pageable);
            responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Medicines retrieved", medicines);
            return new ResponseEntity<>(responseCustom, HttpStatus.OK);
        } catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy chi tiết một thuốc theo ID
    @GetMapping("/medicines/{id}")
    public ResponseEntity<?> getMedicineById(@PathVariable Long id) {
        ResponseCustom responseCustom;
        try {
            MedicineDTO medicine = IMedicineService.getMedicineById(id);
            if (medicine != null) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Medicine details", medicine);
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            } else {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "Medicine not found", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tạo mới một thuốc
    @PostMapping("/medicines")
    public ResponseEntity<?> createMedicine(@RequestBody MedicineDTO medicineDTO) {
        ResponseCustom responseCustom;
        try {
            boolean isCreated = IMedicineService.createMedicine(medicineDTO);
            if (isCreated) {
                responseCustom = new ResponseCustom(HttpStatus.CREATED.value(), "Medicine created successfully", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.CREATED);
            } else {
                responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), "Medicine already exists", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật một thuốc theo ID
    @PutMapping("/medicines/{id}")
    public ResponseEntity<?> updateMedicine(@PathVariable Long id, @RequestBody MedicineDTO medicineDTO) {
        ResponseCustom responseCustom;
        try {
            medicineDTO.setId(id);
            boolean isUpdated = IMedicineService.updateMedicine(medicineDTO);
            if (isUpdated) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Medicine updated successfully", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            } else {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "Medicine not found", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa một thuốc theo ID
    @DeleteMapping("/medicines/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        ResponseCustom responseCustom;
        try {
            boolean isDeleted = IMedicineService.deleteMedicine(id);
            if (isDeleted) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Medicine deleted successfully", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            } else {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "Medicine not found", null);
                return new ResponseEntity<>(responseCustom, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
