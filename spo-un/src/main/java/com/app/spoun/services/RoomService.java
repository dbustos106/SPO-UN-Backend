package com.app.spoun.services;

import com.app.spoun.domain.Building;
import com.app.spoun.domain.Room;
import com.app.spoun.dto.BuildingDTO;
import com.app.spoun.dto.FullRoomDTO;
import com.app.spoun.dto.RoomDTO;
import com.app.spoun.mappers.BuildingMapper;
import com.app.spoun.mappers.RoomMapper;
import com.app.spoun.repository.IBuildingRepository;
import com.app.spoun.repository.IRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
public class RoomService {

    private final IRoomRepository iRoomRepository;
    private final IBuildingRepository iBuildingRepository;
    private final RoomMapper roomMapper;
    private final BuildingMapper buildingMapper;

    @Autowired
    public RoomService(IRoomRepository iRoomRepository,
                       IBuildingRepository iBuildingRepository,
                       RoomMapper roomMapper,
                       BuildingMapper buildingMapper){
        this.iRoomRepository = iRoomRepository;
        this.iBuildingRepository = iBuildingRepository;
        this.roomMapper = roomMapper;
        this.buildingMapper = buildingMapper;
    }


    public Map<String, Object> getAllRoom(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // create list object FullRoomDTO
        List<FullRoomDTO> listFullRoomDTOS = new ArrayList<>();

        // get page of rooms
        Pageable page = PageRequest.of(idPage, size);
        Page<Room> rooms = iRoomRepository.findAll(page);

        // map all rooms
        for(Room room : rooms){
            FullRoomDTO fullRoomDTO = new FullRoomDTO();
            RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);
            Building building = iBuildingRepository.findById(roomDTO.getBuilding_id()).orElse(null);
            BuildingDTO buildingDTO = buildingMapper.buildingToBuildingDTO(building);
            fullRoomDTO.setRoomDTO(roomDTO);
            fullRoomDTO.setBuildingDTO(buildingDTO);
            listFullRoomDTOS.add(fullRoomDTO);
        }

        // return page of rooms
        answer.put("message", listFullRoomDTOS);

        return answer;
    }

    public Map<String, Object> findRoomById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Room room = iRoomRepository.findById(id).orElse(null);
        if(room != null){
            RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);
            answer.put("message", roomDTO);
        }else{
            throw new NotFoundException("Room not found");
        }
        return answer;
    }

    public Map<String, Object> saveRoom(RoomDTO roomDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(roomDTO == null){
            throw new IllegalStateException("Request data missing");
        }else{
            // get building
            Building building = iBuildingRepository.findById(roomDTO.getBuilding_id()).orElse(null);

            // save room
            Room room = roomMapper.roomDTOToRoom(roomDTO);
            room.setBuilding(building);
            room.setAppointments(new ArrayList<>());

            iRoomRepository.save(room);
            answer.put("message", "Room saved successfully");
        }
        return answer;
    }

    public Map<String, Object> editRoom(RoomDTO roomDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(roomDTO == null){
            throw new IllegalStateException("Request data missing");
        }else{
            Room room = iRoomRepository.findById(roomDTO.getId()).orElse(null);
            if(room == null) {
                throw new NotFoundException("Room not found");
            }else{
                // update room
                room.setName(roomDTO.getName());

                iRoomRepository.save(room);
                answer.put("message", "Room updated successfully");
            }
        }
        return answer;
    }

    public Map<String, Object> deleteRoom(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(iRoomRepository.existsById(id)){
            iRoomRepository.deleteById(id);
            answer.put("message", "Room deleted successfully");
        }else{
            throw new NotFoundException("Room not found");
        }
        return answer;
    }

}