package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.Address;
import com.project.entity.User;
import com.project.repository.AddressRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // 🔥 Used while placing order
    public Address getSelectedAddressForUser(User user) {
        return addressRepository.findByUserAndSelectedTrue(user);
    }
    // ✅ Latest address
    public Address getLatestAddress(User user) {
        return addressRepository
                .findByUserOrderByIdDesc(user)
                .stream()
                .findFirst()
                .orElse(null);
    }

    // 🔥 When user selects an address
//    public void selectAddress(User user, Long addressId) {
//
//        List<Address> addresses = addressRepository.findByUser(user);
//
//        for (Address addr : addresses) {
//            addr.setSelected(false);
//        }
//
//        Address selected = addressRepository.findById(addressId)
//                .orElseThrow(() -> new RuntimeException("Address not found"));
//
//        selected.setSelected(true);
//
//        addressRepository.saveAll(addresses);
//        addressRepository.save(selected);
//    }
    @Transactional
    public void selectAddress(User user, Long addressId) {

        List<Address> addresses = addressRepository.findByUser(user);

        for (Address addr : addresses) {
            addr.setSelected(addr.getId().equals(addressId));
        }

        addressRepository.saveAll(addresses);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }
}