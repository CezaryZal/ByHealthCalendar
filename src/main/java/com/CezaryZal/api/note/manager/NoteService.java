package com.CezaryZal.api.note.manager;

import com.CezaryZal.api.note.model.FormNote;
import com.CezaryZal.api.note.model.Header;
import com.CezaryZal.api.note.model.NoteDto;
import com.CezaryZal.api.note.manager.mapper.NoteConverter;
import com.CezaryZal.api.note.manager.repo.NoteRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService{

    private final NoteRepoService noteRepoService;
    private final NoteConverter noteConverter;

    @Autowired
    public NoteService(NoteRepoService noteRepoService,
                       NoteConverter noteConverter) {
        this.noteRepoService = noteRepoService;
        this.noteConverter = noteConverter;
    }

    public FormNote getNoteDtoById(Long id){
        return noteConverter.mappingNoteToDto(noteRepoService.getNoteById(id));
    }

    public String getDetailsById(Long id){
        return noteRepoService.getDetailsNoteById(id);
    }

    public List<Header> getHeadersByDay(Long dayId){
        return noteRepoService.getListHeaderById(dayId);
    }

    public List<FormNote> getNotesDtoByDay(Long dayId){
        return noteConverter.mappingListNoteToListDto(noteRepoService.getNotesByDayId(dayId));
    }

    public List<FormNote> getAllNote (){
        return noteConverter.mappingListNoteToListDto(noteRepoService.getAll());
    }

    public String addNoteByDto (NoteDto noteDto){
        noteRepoService.addNote(noteConverter.mappingDtoToNote(noteDto));
        return "Przesłana notatka została zapisana w bazie danych";
    }

    public String updateNoteByDto (NoteDto noteDto){
        noteRepoService.updateNote(noteConverter.mappingDtoToNote(noteDto));
        return "Przesłana notatka została uaktualniona";
    }

    public String deleteNoteDtoById (Long id){
        noteRepoService.deleteNoteById(id);
        return "Notatka o przesłanym id została usunieta";
    }
}
