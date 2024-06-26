package com.invoiceapi.services.interfaces;

import com.invoiceapi.models.dtos.BusinessDto;
import com.invoiceapi.models.response.BusinessResponse;

public interface BusinessService {
    BusinessResponse createBusiness(BusinessDto businessDto, String email);
    BusinessResponse updateBusiness(Long id, BusinessDto businessDto, String email);
    void deleteBusiness(Long id, String email);
    BusinessResponse getBusinessById(Long id, String userEmail);

    BusinessResponse getBusinessByUser(String email);
}
