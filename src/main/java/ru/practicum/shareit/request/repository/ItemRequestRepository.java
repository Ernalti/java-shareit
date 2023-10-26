package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

	List<ItemRequest> findAllByRequestorNotLike(User user, PageRequest pageRequest);

	List<ItemRequest> findAllByRequestor(User user);
}
