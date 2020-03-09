pragma solidity ^0.4.24;


contract Simple {

    uint version = 2;

    function get() public view returns (uint id){
        return version;
    }

    function set(uint _version) public {
        version = _version;
    }

}