package com.app.spoun.services;

import com.app.spoun.dao.RoomDAO;
import com.app.spoun.dto.RoomDTO;
import com.app.spoun.mappers.RoomMapper;
import com.app.spoun.mappers.RoomMapperImpl;
import com.app.spoun.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class RoomService {
    @Autowired
    private IRoomRepository iRoomRepository;

    private RoomMapper roomMapper = new RoomMapperImpl();

    public Map<String,Object> getAllRoom (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<RoomDAO> roomsDAO = iRoomRepository.findAll(page);

        List<RoomDTO> listRoomsDTO = new ArrayList<>();
        for(RoomDAO roomDAO: roomsDAO){
            RoomDTO roomDTO = roomMapper.roomDAOToRoomDTO(roomDAO);
            listRoomsDTO.add(roomDTO);
        }
        Page<RoomDTO> roomsDTO = new PageImpl<>(listRoomsDTO);

        if(roomsDTO.getSize() != 0){
            answer.put("rooms", roomsDTO);
        }else {
            answer.put("error", "None room found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        RoomDAO roomDAO = iRoomRepository.findById(id).orElse(null);
        RoomDTO roomDTO = roomMapper.roomDAOToRoomDTO(roomDAO);
        if(roomDTO != null){
            answer.put("room", roomDTO);
        }else{
            answer.put("error", "Room not found");
        }
        return answer;
    }

    public Map<String,Object> saveRoom(RoomDTO roomDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roomDTO != null){
            RoomDAO roomDAO = roomMapper.roomDTOToRoomDAO(roomDTO);
            iRoomRepository.save(roomDAO);
            answer.put("room", "Room saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editRoom(RoomDTO roomDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roomDTO.getId() != null && iRoomRepository.existsById(roomDTO.getId())){
            RoomDAO roomDAO = roomMapper.roomDTOToRoomDAO(roomDTO);
            iRoomRepository.save(roomDAO);
            answer.put("room", "Room updated successfully");
        }else{
            answer.put("error", "Room not found");
        }
        return answer;
    }

    public Map<String,Object> deleteRoom(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iRoomRepository.existsById(id)){
            iRoomRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Room not found");
        }
        return answer;
    }

}