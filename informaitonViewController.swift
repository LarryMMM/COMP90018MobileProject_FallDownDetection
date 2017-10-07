//
//  informaitonViewController.swift
//  falldown
//
//  Created by Cong on 7/10/17.
//  Copyright © 2017 Microsoft. All rights reserved.
//

import UIKit

class informaitonViewController: UIViewController {

    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var ageTextField: UITextField!
    @IBOutlet weak var phoneTextField: UITextField!
    
    var table : MSTable?
    var userID:String = ""
    var itemID:String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //connect to table:userData
        let client = MSClient(applicationURLString: "https://falldown.azurewebsites.net")
        table = client.table(withName: "userData")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveButtonTapped(_ sender: Any) {
        let userName = nameTextField.text
        let userAge = ageTextField.text
        let userPhone = phoneTextField.text
        
        if((userName?.isEmpty)! || (userAge?.isEmpty)! || (userPhone?.isEmpty)!){
            displayMyAlertMessage(userMessage: "All fields are required")
            return
        }
        
        let userID = UserDefaults.standard.string(forKey: "loggedInID")!
        

        //upload user informaiton
        table?.update(["id": userID, "name": userName, "Age": userAge, "phone": userPhone]) { (result, error) in
            if let err = error {
                print("ERROR ", err)
            } else if let item = result {
                print("Todo Item: ", item["id"])
            }
        }
        
        


        UserDefaults.standard.set(true, forKey: "hadInformation")
        UserDefaults.standard.synchronize()
        self.dismiss(animated: true, completion: nil)
                
    }
    
    @IBAction func cancelButtonTapped(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    func displayMyAlertMessage(userMessage:String)
    {
        let myAlert = UIAlertController(title:"Sorry", message:userMessage, preferredStyle: UIAlertControllerStyle.alert);
        
        let okAction = UIAlertAction(title:"Ok", style:UIAlertActionStyle.default, handler:nil);
        
        myAlert.addAction(okAction);
        
        self.present(myAlert, animated:true, completion:nil);
        
    }

}
