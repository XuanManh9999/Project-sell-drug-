package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.BillDTO;
import com.back_end.myProject.dto.DetailBillDTO;
import com.back_end.myProject.dto.RevenueStatisticsDTO;
import com.back_end.myProject.entities.Bill;
import com.back_end.myProject.entities.DetailBill;
import com.back_end.myProject.entities.Medicine;
import com.back_end.myProject.repositorys.BillRepository;
import com.back_end.myProject.repositorys.DetailBillRepository;
import com.back_end.myProject.repositorys.MedicineRepository;
import com.back_end.myProject.repositorys.UserRepository;
import com.back_end.myProject.service.IBill;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillServiceImpl implements IBill {

    private final DetailBillRepository detailBillRepository;
    private final ModelMapper modelMapper;
    private final BillRepository billRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    public BillServiceImpl(BillRepository billRepository, MedicineRepository medicineRepository, ModelMapper modelMapper, DetailBillRepository detailBillRepository, UserRepository userRepository)
    {
        this.billRepository = billRepository;
        this.modelMapper = new ModelMapper();
        this.medicineRepository = medicineRepository;
        this.detailBillRepository = detailBillRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean createBill(BillDTO billDTO) {
        Bill bill = new Bill();
        bill.setName(billDTO.getName());
        bill.setCustomerName(billDTO.getCustomer_name());
        bill.setCustomer_phone(billDTO.getCustomer_phone());
        bill.setStatus(1);

        // Lưu hóa đơn trước khi thêm chi tiết hóa đơn
        Bill savedBill = billRepository.save(bill);

        if (billDTO.getDetailBills() != null) {
            List<DetailBillDTO> detailBillDTOS = billDTO.getDetailBills();
            for (DetailBillDTO detailBillDTO : detailBillDTOS) {
                DetailBill detailBill = new DetailBill();
                // Tìm thuốc theo ID
                Optional<Medicine> medicineOptional = medicineRepository.findById(detailBillDTO.getId_medicine());
                if (medicineOptional.isPresent()) {
                    Medicine medicine = medicineOptional.get();

                    // Kiểm tra nếu số lượng thuốc đủ để bán
                    if (medicine.getQuantity() >= detailBillDTO.getQuantity()) {
                        // Trừ đi số lượng thuốc
                        double newQuantity = medicine.getQuantity() - detailBillDTO.getQuantity();
                        medicine.setQuantity(newQuantity);

                        // Lưu thuốc đã cập nhật số lượng
                        medicineRepository.save(medicine);

                        // Lưu chi tiết hóa đơn
                        detailBill.setMedicine(medicine);
                        detailBill.setQuantity(detailBillDTO.getQuantity());
                        detailBill.setBill(savedBill);  // Gán hóa đơn đã lưu
                        detailBill.setStatus(1);
                        detailBillRepository.save(detailBill);
                    } else {
                        // Xử lý khi số lượng thuốc không đủ để bán
                        System.out.println("Không đủ số lượng thuốc với ID: " + detailBillDTO.getId_medicine());
                    }
                } else {
                    // Xử lý khi không tìm thấy thuốc, có thể thêm thông báo lỗi
                    System.out.println("Không tìm thấy thuốc với ID: " + detailBillDTO.getId_medicine());
                }
            }
        }

        // Kiểm tra xem bill đã được lưu thành công chưa
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

    @Override
    public BillDTO detailBill(Long id) {
        return billRepository.detailBill(id);
    }

    @Override
    public RevenueStatisticsDTO statistics() {
        Long billCount = (Long) billRepository.count();
        double totalRevenue = detailBillRepository.calculateTotalRevenue();
        Long soldProductCount = (Long) detailBillRepository.countSoldProducts();
        Long accountCount = (Long) userRepository.count();
        RevenueStatisticsDTO revenueStatisticsDTO = new RevenueStatisticsDTO();
        revenueStatisticsDTO.setTotalBills(billCount);
        revenueStatisticsDTO.setTotalRevenue(totalRevenue);
        revenueStatisticsDTO.setTotalProductsSold(soldProductCount);
        revenueStatisticsDTO.setTotalAccounts(accountCount);
        return revenueStatisticsDTO;
    }

}
