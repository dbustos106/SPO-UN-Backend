package com.app.spoun.services;

import com.app.spoun.domain.Building;
import com.app.spoun.domain.Room;
import com.app.spoun.dto.RoomDTO;
import com.app.spoun.mappers.RoomMapper;
import com.app.spoun.mappers.RoomMapperImpl;
import com.app.spoun.repository.IBuildingRepository;
import com.app.spoun.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
public class RoomService {
    @Autowired
    private IRoomRepository iRoomRepository;

    @Autowired
    private IBuildingRepository iBuildingRepository;

    private RoomMapper roomMapper = new RoomMapperImpl();

    public Map<String,Object> getAllRoom (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Room> rooms = iRoomRepository.findAll(page);

        List<RoomDTO> listRoomsDTO = new ArrayList<>();
        for(Room room : rooms){
            RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);
            listRoomsDTO.add(roomDTO);
        }
        Page<RoomDTO> roomsDTO = new PageImpl<>(listRoomsDTO);

        if(roomsDTO.getSize() != 0){
            answer.put("message", roomsDTO);
        }else {
            answer.put("error", "None room found");
        }
        return answer;
    }

    public Map<String,Object> findRoomById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Room room = iRoomRepository.findById(id).orElse(null);
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);
        if(roomDTO != null){
            answer.put("message", roomDTO);
        }else{
            answer.put("error", "Room not found");
        }
        return answer;
    }

    public Map<String,Object> saveRoom(RoomDTO roomDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roomDTO != null){
            Building building = iBuildingRepository.findById(roomDTO.getBuilding_id()).orElse(null);
            Room room = roomMapper.roomDTOToRoom(roomDTO);
            room.setBuilding(building);
            iRoomRepository.save(room);
            answer.put("message", "Room saved successfully");
        }else{
            answer.put("error", "Room not saved");
        }
        return answer;
    }

    public Map<String,Object> editRoom(RoomDTO roomDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roomDTO.getId() != null && iRoomRepository.existsById(roomDTO.getId())){
            Building building = iBuildingRepository.findById(roomDTO.getBuilding_id()).orElse(null);
            Room room = roomMapper.roomDTOToRoom(roomDTO);
            room.setBuilding(building);
            iRoomRepository.save(room);
            answer.put("message", "Room updated successfully");
        }else{
            answer.put("error", "Room not found");
        }
        return answer;
    }

    public Map<String,Object> deleteRoom(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iRoomRepository.existsById(id)){
            iRoomRepository.deleteById(id);
            answer.put("message", "Room deleted successfully");
        }else{
            answer.put("error", "Room not found");
        }
        return answer;
    }

}