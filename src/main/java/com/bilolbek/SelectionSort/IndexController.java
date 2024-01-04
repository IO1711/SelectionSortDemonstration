package com.bilolbek.SelectionSort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private final NumbersRepository numbersRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public IndexController(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/index")
    public String index1(){
        return "index";
    }



    @GetMapping("/index/findMin")
    public ResponseEntity<Integer> findMin(@RequestParam int low,@RequestParam int high){


        Numbers numbers=new Numbers();

        numbers=numbersRepository.findById(low)
                .orElseThrow(() -> new ResourceNotFoundException("no numbers"));



        int min=numbers.getNumbers();
        int indexMin=low;



        //find the smallest number
        for(int i=low+1;i<=high;i++){
            numbers=numbersRepository.findById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("no numbers"));

            if(min>numbers.getNumbers()){
                min=numbers.getNumbers();
                indexMin=i;
            }
        }

        //change the smallest number with the first not sorted element
        numbers = numbersRepository.findById(indexMin)
                .orElseThrow(() -> new ResourceNotFoundException("error in finding numbers"));

        Numbers temp = numbersRepository.findById(low)
                .orElseThrow(() -> new ResourceNotFoundException("Low does not exist"));

        numbers.setNumbers(temp.getNumbers());

        numbersRepository.save(numbers);

        temp.setNumbers(min);

        numbersRepository.save(temp);



        return new ResponseEntity<>(indexMin, HttpStatus.OK);
    }

    @GetMapping("/index/isSorted")
    public ResponseEntity<Boolean> isSorted(){
        Numbers numbers;
        Numbers numbers1;

        for(int i = 1; i<8; i++){
            numbers = numbersRepository.findById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found"));
            numbers1 = numbersRepository.findById(i+1)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found"));
            if(numbers.getNumbers()>numbers1.getNumbers()){

                return ResponseEntity.ok(false);
            }
        }

        return ResponseEntity.ok(true);
    }


    @GetMapping("/index/getList")
    public ResponseEntity<String> getList(){
        Numbers numbers;
        List<Numbers> numbersList = new ArrayList<>();

        for(int i=1; i<=numbersRepository.count(); i++){
            numbers = numbersRepository.findById(i)
                    .orElseThrow(() -> new ResourceNotFoundException("Error"));

            numbersList.add(numbers);
        }

        try{
            String numbersAsJson = objectMapper.writeValueAsString(numbersList);

            return ResponseEntity.ok(numbersAsJson);
        }
        catch (JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting numbers into Json");
        }

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @PostMapping("/index")
    public void postIndex(@RequestParam(name = "numbers") int[] numbers){
        if(numbersRepository.existsById(1)){

            for(int number=1;number<9;number++){

                Numbers numbers1=numbersRepository.findById(number)
                        .orElseThrow(() -> new ResourceNotFoundException("Number not found"));

                numbers1.setNumbers(numbers[number-1]);
                numbersRepository.save(numbers1);

            }
        }
        else {

            for (int number : numbers) {
                Numbers numbers1;
                numbers1 = new Numbers();

                numbers1.setNumbers(number);
                numbersRepository.save(numbers1);
            }
        }

    }



    @DeleteMapping("/index/delete")
    public void cleanse(){
            for(int i=1;i<9;i++){
            numbersRepository.deleteById(i);
            }

    }

}
