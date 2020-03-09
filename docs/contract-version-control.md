
<br/>

## Target：

- support contract upgrade.

- support add the specified version of contract.

- support get addresses of all versions of contract.

- support get address of specified version of contract.

- support delete contract.

- support get all versions of contract.


<br/>

## Design:

- Use blockchain evidence to store contract info, use the reback address as the unique Identification of the contract.

- Use map to record between contract_version and contract_address, as version_contract_address_map.

- Use map to record betwcontract_info_evidence_address and version_contract_address_map.

![](https://cdn.img.wenhairu.com/images/2020/03/07/mMTiH.png "")
<br/>

## Code:

```text
pragma solidity ^0.4.24;

contract ControlVersion {

    struct AddressMap
    {
        mapping(address => IndexValue[]) data;
        KeyFlag[] keys;
        uint size;
    }
    struct IndexValue { uint key; address value; } // key represent version
    struct KeyFlag { address key; bool deleted; }
  
   AddressMap address_map;
   
    constructor() public
    {
        address_map.size = 0;
    }
  
    function contains_key(address key) public view returns (bool containsed)
    {
        if(address_map.data[key].length > 0){
            return true;
        }
        return false;
    }
  
    function getIndex(address key, uint version) public view returns (int id)
    {
        bool key_exist = contains_key(key);
        if(key_exist){
            IndexValue[] memory indexValues = address_map.data[key];
            for(uint j = 0;j<indexValues.length;j++){
                if(indexValues[j].key == version){
                   return int(j);
                }
            }
            return -1;
        }
        return -2;
    }
  
    function add_address(address key, uint version, address value) public returns (bool replaced)
    {
        int index = getIndex(key,version);
        
        // init IndexValues
        IndexValue indexValue;
        indexValue.key = version;
        indexValue.value = value;
        
        if(index == -2){
            
            // init KeyFlag
            KeyFlag memory keyFlag;
            keyFlag.key = key;
            keyFlag.deleted = false;
            
            // init IndexValues
            IndexValue[] indexValuesTmp;
            indexValuesTmp[0]=indexValue;
            
            // init data
            address_map.data[key] = indexValuesTmp;
            address_map.keys[address_map.size] = keyFlag;
            address_map.size++;
            
            return false;
        }
        
        if(index == -1){
            address_map.data[key].push(indexValue);
            return false;
        } else {
            address_map.data[key][uint(index)].value = value;
            return true;
        }
        return false;
    }
    
    function remove_address(address key) public returns (bool removed)
    {

        for(uint i = 0;i<address_map.keys.length;i++){
            address key_tmp =  address_map.keys[i].key;
            if(key_tmp == key){
               delete address_map.data[key];
               address_map.keys[i].deleted = true;
               return true;
           }
        }
        return false;
    }
  
    function contains_key_index(address key, uint version) public view returns (bool success)
    {
        int id = getIndex(key, version);
        if(id>=0){
            return true;
        }
        return false;
    }
  
    function get_address_list(address key) public view returns (address[] addr)
    {
      
        bool key_exist = contains_key(key);
        address[] addr_list;
        if(key_exist){
            for(uint i=0; i<address_map.data[key].length; i++){
                addr_list[i] = address_map.data[key][i].value;
            }
        }
        return addr_list;
    }
  
    function get_address(address key, uint version) public view returns (address addr)
    {
        int id = getIndex(key, version);
        if(id>=0){
            return address_map.data[key][uint(id)].value;
        }
        return address(0);
    }

}

```

