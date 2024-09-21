package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.BillDTO;
import com.back_end.myProject.dto.DetailBillDTO;
import com.back_end.myProject.entities.Bill;
import com.back_end.myProject.entities.DetailBill;
import com.back_end.myProject.entities.Medicine;
import com.back_end.myProject.repositorys.BillRepository;
import com.back_end.myProject.repositorys.MedicineRepository;
import com.back_end.myProject.service.IBill;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements IBill {

    private final ModelMapper modelMapper;
    private final BillRepository billRepository;
    private final MedicineRepository medicineRepository;
    public BillServiceImpl(BillRepository billRepository, MedicineRepository medicineRepository, ModelMapper modelMapper)
    {
        this.billRepository = billRepository;
        this.modelMapper = new ModelMapper();
        this.medicineRepository = medicineRepository;
    }

    @Override
    public boolean creayeBill(BillDTO billDTO) {
        Bill bill = new Bill();
        bill.setName(billDTO.getName());
        bill.setCustomerName(billDTO.getCustomer_name());
        bill.setCustomer_phone(billDTO.getCustomer_phone());
        bill.setStatus(1);

        if (bill.getListDetailBill() != null) {
            List<DetailBillDTO> detailBillDTOS = (List<DetailBillDTO>) billDTO.getDetailBill();
            List<DetailBill> detailBills = new ArrayList<>();
            for (DetailBillDTO detailBillDTO : detailBillDTOS) {
                DetailBill detailBill = new DetailBill();
                detailBill.setQuantity(detailBillDTO.getQuantity());
                Optional<Medicine> medicine = medicineRepository.findById(detailBillDTO.getId_medicine());
                detailBill.setMedicine(medicine.orElse(new Medicine()));
                detailBill.setStatus(1);
                detailBill.setBill(bill);  // Liên kết DetailBill với Bill
                detailBills.add(detailBill);
            }
            bill.setListDetailBill(detailBills);
        }
        Bill savedBill = billRepository.save(bill);
        // Kiểm tra xem bill đã được lưu thành công chưa (thông qua ID hoặc thuộc tính khác)
        return savedBill.getId() != null;
    }

    @Override
    public boolean updateBill(BillDTO billDTO) {
        Optional<Bill> optionalBill = billRepository.findById(billDTO.getId());

        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setName(billDTO.getName());
            bill.setCustomerName(billDTO.getCustomer_name());
            bill.setCustomer_phone(billDTO.getCustomer_phone());
            bill.setStatus(1);
            billRepository.save(bill); // nem ra mot exception
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean deleteBill(Long id) {
        var isExist = billRepository.existsById(id);
        if (isExist) {
            billRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Page<BillDTO> getAllBills(Pageable pageable) {
        // Lấy trang dữ liệu Bill từ repository
        Page<Bill> billsPage = billRepository.findAll(pageable);

        // Ánh xạ từng Bill sang BillDTO và bao gồm cả danh sách DetailBillDTO
        return billsPage.map(bill -> {
            // Ánh xạ Bill sang BillDTO
            BillDTO billDTO = modelMapper.map(bill, BillDTO.class);
            billDTO.setCustomer_name(bill.getCustomerName());

            // Ánh xạ danh sách DetailBill sang DetailBillDTO
            List<DetailBillDTO> detailBillDTOS = bill.getListDetailBill().stream()
                    .map(detailBill -> modelMapper.map(detailBill, DetailBillDTO.class))
                    .collect(Collectors.toList());

            // Gán danh sách DetailBillDTO vào BillDTO
            billDTO.setDetailBill(detailBillDTOS);

            return billDTO;
        });
    }


    @Override
    public BillDTO findBillById(Long id) {
        Optional<Bill> billOpt = billRepository.findById(id); // Tìm bill theo id
        if (billOpt.isPresent()) {
            Bill bill = billOpt.get();

            // Sử dụng modelMapper để ánh xạ dữ liệu từ Bill sang BillDTO
            BillDTO billDTO = modelMapper.map(bill, BillDTO.class);

            // Ánh xạ danh sách DetailBill sang DetailBillDTO
            List<DetailBillDTO> detailBillDTOS = bill.getListDetailBill().stream()
                    .map(detailBill -> modelMapper.map(detailBill, DetailBillDTO.class))
                    .collect(Collectors.toList());
            billDTO.setCustomer_name(bill.getCustomerName());
            // Gán danh sách DetailBillDTO vào BillDTO
            billDTO.setDetailBill(detailBillDTOS);

            return billDTO;
        } else {
            // Nên xử lý tình huống không tìm thấy bill (có thể ném ngoại lệ hoặc trả về giá trị khác)
            throw new EntityNotFoundException("Bill not found with id: " + id);
        }
    }

    @Override
    public List<BillDTO> searchBill(BillDTO billDTO) {
        return billRepository.searchBill(billDTO);
    }

}
