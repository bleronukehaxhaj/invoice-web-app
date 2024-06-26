package com.invoiceapi.services;

import com.invoiceapi.exceptions.DuplicateBusinessException;
import com.invoiceapi.mappers.BusinessMapper;
import com.invoiceapi.models.dtos.BusinessDto;
import com.invoiceapi.models.entities.Business;
import com.invoiceapi.models.response.BusinessResponse;
import com.invoiceapi.repositories.BusinessRepository;
import com.invoiceapi.repositories.UserRepository;
import com.invoiceapi.services.interfaces.BusinessService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final UserRepository userRepository;

    @Override
    public BusinessResponse createBusiness(BusinessDto businessDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Business business = businessMapper.toEntityFromDto(businessDto);
        business.setUser(user);
        try {
            var savedBusiness = businessRepository.save(business);
            return businessMapper.toResponseFromEntity(savedBusiness);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateBusinessException("Duplicate key error: " + e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public BusinessResponse updateBusiness(Long id, BusinessDto businessDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));
        if (!business.getUser().equals(user)) {
            throw new SecurityException("User not authorized to update this business");
        }


        business.setName(businessDto.name());
        business.setAddress(businessDto.address());
        business.setEmail(businessDto.email());
        business.setPhone(businessDto.phone());

        var updatedBusiness = businessRepository.save(business);
        return businessMapper.toResponseFromEntity(updatedBusiness);
    }

    @Override
    public void deleteBusiness(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));
        if (!business.getUser().equals(user)) {
            throw new SecurityException("User not authorized to update this business");
        }
        businessRepository.delete(business);
    }

    @Override
    public BusinessResponse getBusinessById(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));
        if (!business.getUser().equals(user)) {
            throw new SecurityException("User not authorized to update this business");
        }

        return businessMapper.toResponseFromEntity(business);
    }

    @Override
    public BusinessResponse getBusinessByUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var business = businessRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        return businessMapper.toResponseFromEntity(business);
    }
}
