package com.app.spoun.services;

import com.app.spoun.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.spoun.models.Room;

import java.util.Map;
import java.util.TreeMap;


@Service
public class RoomService {
    @Autowired
    private IRoomRepository iRoomRepository;

    public Map<String,Object> getAllRoom (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Room> rooms = iRoomRepository.findAll(page);
        if(rooms.getSize() != 0){
            answer.put("rooms", rooms);
        }else {
            answer.put("error", "No room found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Room room = iRoomRepository.findById(id).orElse(null);
        if(room != null){
            answer.put("room", room);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Room not found");
        }
        return answer;
    }

    public Map<String,Object> saveRoom(Room room){
        Map<String,Object> answer = new TreeMap<>();
        if(room != null){
            System.out.println("Guardar room");
            Room room_answer = iRoomRepository.save(room);
            answer.put("room", room_answer);
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editRoom(Room room){
        Map<String,Object> answer = new TreeMap<>();
        if(room.getId() != null && iRoomRepository.existsById(room.getId())){
            Room room_answer = iRoomRepository.save(room);
            answer.put("room", room_answer);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Room not found");
        }
        return answer;
    }

    public Map<String,Object> deleteRoom(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iRoomRepository.existsById(id)){
            iRoomRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Room not found");
        }
        return answer;
    }

    public boolean existById(Integer id){
        return iRoomRepository.existsById(id);
    }

}